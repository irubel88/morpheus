/*
 * Copyright 2014 websudos ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.websudos.morpheus.query

import scala.concurrent.{Future => ScalaFuture}

import com.twitter.util.Future
import com.websudos.morpheus.{Row, Result, Client}
import com.websudos.morpheus.dsl.{ResultSetOperations, BaseTable}

case class SQLBuiltQuery(queryString: String) {
  def append(st: String): SQLBuiltQuery = SQLBuiltQuery(queryString + st)
  def append(st: SQLBuiltQuery): SQLBuiltQuery = append(st.queryString)

  def appendEscape(st: String): SQLBuiltQuery = append(escape(st))
  def appendEscape(st: SQLBuiltQuery): SQLBuiltQuery = append(escape(st.queryString))

  def prepend(st: String): SQLBuiltQuery = SQLBuiltQuery(st + queryString)
  def prepend(st: SQLBuiltQuery): SQLBuiltQuery = prepend(st.queryString)

  def escape(st: String): String = "'" + st + "'"

  def spaced: Boolean = queryString.endsWith(" ")
  def pad: SQLBuiltQuery = if (spaced) this else SQLBuiltQuery(queryString + " ")
  def forcePad: SQLBuiltQuery = SQLBuiltQuery(queryString + " ")
  def trim: SQLBuiltQuery = SQLBuiltQuery(queryString.trim)

  def wrap(str: String): SQLBuiltQuery = pad.append(DefaultSQLSyntax.`(`).append(str).append(DefaultSQLSyntax.`)`)
  def wrap(query: SQLBuiltQuery): SQLBuiltQuery = wrap(query.queryString)


}


trait SQLQuery[T <: BaseTable[T, _], R] extends ResultSetOperations {
  protected[morpheus] val query: SQLBuiltQuery

  /**
   * A simple forwarding method to prevent some extra boiler-plate during tests.
   * This will serialise an existing query to the relevant SQL string.
   * @return A string representing the query encoded in SQL.
   */
  def queryString: String = query.queryString

  /**
   * This method is used when the query is not returning a data result, such as an UPDATE query.
   * While it will accurately monitor the execution of the query and the Future will complete when the task is "done" in SQL,
   * the type-safe mapping of the result is not necessary at this point.
   *
   * This method duplicates the API to provide an alternative to people who don't use Twitter Futures or any Twitter libraries in their stack. Until now
   * anyway. Twitter has been gossiping about making com.twitter.util.Future extend scala.concurrent.Future, but until such times a dual API is best.
   *
   * The below implementation will pass type arguments explicitly to the covariant constructor.
   *
   * @param client The Finagle MySQL client in the scope of which to execute the query.
   * @return A Scala Future wrapping a default Finagle MySQL query result object.
   */
  def future[DBRow <: Row, DBResult <: Result]()(implicit client: Client[DBRow, DBResult]): ScalaFuture[DBResult] = {
    queryToScalaFuture[DBRow, DBResult](query.queryString)
  }

  /**
   * This method is used when the query is not returning a data result, such as an UPDATE query.
   * While it will accurately monitor the execution of the query and the Future will complete when the task is "done" in SQL,
   * the type-safe mapping of the result is not necessary at this point.
   *
   * The below implementation will pass type arguments explicitly to the covariant constructor.
   *
   * @param client The Finagle MySQL client in the scope of which to execute the query.
   * @return A Scala Future wrapping a default Finagle MySQL query result object.
   */
  def execute[DBRow <: Row, DBResult <: Result]()(implicit  client: Client[DBRow, DBResult]): Future[DBResult] = {
    queryToFuture[DBRow, DBResult](query.queryString)
  }

}


trait SQLResultsQuery[T <: BaseTable[T, _], R, DBRow <: Row, DBResult <: Result] extends SQLQuery[T, R] {
  def fromRow(r: Row): R

  /**
   * Returns the first row from the select ignoring everything else.
   * @param client The MySQL client in use.
   * @return
   */
  def one()(implicit client: Client[DBRow, DBResult]): ScalaFuture[Option[R]]

  /**
   * Get the result of an operation as a Twitter Future.
   * @param client The MySQL client in use.
   * @return A Twitter future wrapping the result.
   */
  def get()(implicit client: Client[DBRow, DBResult]): Future[Option[R]]
}

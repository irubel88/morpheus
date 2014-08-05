/*
 *
 *  * Copyright 2014 websudos ltd.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.websudos.morpheus.mysql

import com.websudos.morpheus.dsl.Table
import com.websudos.morpheus.query.{AbstractDeleteSyntaxBlock, DefaultSQLSyntax, SQLBuiltQuery}

case class MySQLDeleteSyntaxBlock[T <: Table[T, _], R](query: String, tableName: String,
                                                       columns: List[String] = List("*")) extends AbstractDeleteSyntaxBlock(query, tableName,
  columns) {

  val syntax = MySQLSyntax

  def lowPriority: SQLBuiltQuery = {
    qb.pad.append(DefaultSQLSyntax.lowPriority)
      .forcePad.append(DefaultSQLSyntax.from)
      .forcePad.append(tableName)
  }

  def ignore: SQLBuiltQuery = {
    qb.pad.append(DefaultSQLSyntax.ignore)
      .forcePad.append(DefaultSQLSyntax.from)
      .forcePad.append(tableName)
  }

  def quick: SQLBuiltQuery = {
    qb.pad.append(DefaultSQLSyntax.quick)
      .forcePad.append(DefaultSQLSyntax.from)
      .forcePad.append(tableName)
  }
}

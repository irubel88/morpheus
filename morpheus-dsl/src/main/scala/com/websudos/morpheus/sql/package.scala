/*
 * Copyright 2014 websudos ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.websudos.morpheus

import com.websudos.morpheus.column.AbstractColumn
import com.websudos.morpheus.dsl.{DefaultImportsDefinition, BaseTable => BaseTable}
import com.websudos.morpheus.operators.SQLOperatorSet
import com.websudos.morpheus.query.{DefaultSQLEngines, SQLQueryColumn}

package object sql extends DefaultImportsDefinition with SQLPrimitives with DefaultSQLEngines with SQLOperatorSet {
  override implicit def columnToQueryColumn[T : SQLPrimitive](col: AbstractColumn[T]): SQLQueryColumn[T] = new SQLQueryColumn[T](col)

  type Table[Owner <: BaseTable[Owner, Record], Record] = com.websudos.morpheus.sql.SQLTable[Owner, Record]
}

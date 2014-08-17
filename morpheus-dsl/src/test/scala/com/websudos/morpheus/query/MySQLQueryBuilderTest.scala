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

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

import com.websudos.morpheus.query.mysql.MySQLQueryBuilder


class MySQLQueryBuilderTest extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {

  it should "serialise a simple equals condition" in {
    forAll(minSuccessful(300)) { (name: String, value: String) =>
      whenever (name.length > 0 && value.length > 0) {
        val query = MySQLQueryBuilder.eqs(name, value).queryString
        query shouldEqual s"$name = $value"
      }
    }
  }

  it should "serialise a simple lt condition" in {
    forAll(minSuccessful(300)) { (name: String, value: String) =>
      whenever (name.length > 0 && value.length > 0) {
        val query = MySQLQueryBuilder.lt(name, value).queryString
        query shouldEqual s"$name < $value"
      }
    }
  }

  it should "serialise a simple lte condition" in {
    forAll(minSuccessful(300)) { (name: String, value: String) =>
      whenever (name.length > 0 && value.length > 0) {
        val query = MySQLQueryBuilder.lte(name, value).queryString
        query shouldEqual s"$name <= $value"
      }
    }
  }

  it should "serialise a simple gt condition" in {
    forAll(minSuccessful(300)) { (name: String, value: String) =>
      whenever (name.length > 0 && value.length > 0) {
        val query = MySQLQueryBuilder.gt(name, value).queryString
        query shouldEqual s"$name > $value"
      }
    }
  }

  it should "serialise a simple gte condition" in {
    forAll(minSuccessful(300)) { (name: String, value: String) =>
      whenever (name.length > 0 && value.length > 0) {
        val query = MySQLQueryBuilder.gte(name, value).queryString
        query shouldEqual s"$name >= $value"
      }
    }
  }

  it should "serialise a simple != condition" in {
    forAll(minSuccessful(300)) { (name: String, value: String) =>
      whenever (name.length > 0 && value.length > 0) {
        val query = MySQLQueryBuilder.!=(name, value).queryString
        query shouldEqual s"$name != $value"
      }
    }
  }

  it should "serialise a simple <> condition" in {
    forAll(minSuccessful(300)) { (name: String, value: String) =>
      whenever (name.length > 0 && value.length > 0) {
        val query = MySQLQueryBuilder.<>(name, value).queryString
        query shouldEqual s"$name <> $value"
      }
    }
  }

  it should "serialise a simple <=> condition" in {
    forAll(minSuccessful(300)) { (name: String, value: String) =>
      whenever (name.length > 0 && value.length > 0) {
        val query = MySQLQueryBuilder.<=>(name, value).queryString
        query shouldEqual s"$name <=> $value"
      }
    }
  }

  it should "serialise a simple select * query" in {
    forAll(minSuccessful(300)) { (name: String) =>
      whenever (name.length > 0) {
        val query = MySQLQueryBuilder.select(name).queryString
        query shouldEqual s"SELECT * FROM $name"
      }
    }
  }

  it should "serialise a partial select query where 1 column name is specified" in {
    forAll(minSuccessful(300)) { (name: String, column: String) =>
      whenever (name.length > 0 && column.length > 0) {
        val query = MySQLQueryBuilder.select(name, column).queryString
        query shouldEqual s"SELECT $column FROM $name"
      }
    }
  }

  it should "serialise a partial select query where 2 column names are specified" in {
    forAll(minSuccessful(300)) { (name: String, column1: String, column2: String) =>
      whenever (name.length > 0 && column1.length > 0 && column2.length > 0) {
        val query = MySQLQueryBuilder.select(name, column1, column2).queryString
        query shouldEqual s"SELECT $column1 $column2 FROM $name"
      }
    }
  }

  it should "serialise a partial select query where 3 column names are specified" in {
    forAll(minSuccessful(300)) { (name: String, column1: String, column2: String, column3: String) =>
      whenever (name.length > 0 && column1.length > 0 && column2.length > 0 && column3.length > 0) {
        val query = MySQLQueryBuilder.select(name, column1, column2, column3).queryString
        query shouldEqual s"SELECT $column1 $column2 $column3 FROM $name"
      }
    }
  }

  it should "serialise an IN operator query" in {
    forAll(minSuccessful(300)) { (name: String, column1: String, column2: String, column3: String) => {
      whenever(name.length > 0 && column1.length > 0 && column2.length > 0 && column3.length > 0) {
        val query = MySQLQueryBuilder.in(name, List(column1, column2, column3)).queryString
        query shouldEqual s"$name IN ($column1, $column2, $column3)"
      }
    }
    }
  }

  it should "serialise an NOT IN operator query" in {
    forAll(minSuccessful(300)) { (name: String, column1: String, column2: String, column3: String) => {
      whenever(name.length > 0 && column1.length > 0 && column2.length > 0 && column3.length > 0) {
        val query = MySQLQueryBuilder.notIn(name, List(column1, column2, column3)).queryString
        query shouldEqual s"$name NOT IN ($column1, $column2, $column3)"
      }
    }
    }
  }

  it should "serialise an setTo operator query" in {
    forAll(minSuccessful(300)) { (name: String, value: String) =>
      whenever (name.length > 0) {
        val query = MySQLQueryBuilder.setTo(name, value).queryString
        query shouldEqual s"$name = $value"
      }
    }
  }


  it should "serialise an ASC operator query" in {
    forAll(minSuccessful(300)) { (name: String) =>
      whenever (name.length > 0) {
        val query = MySQLQueryBuilder.asc(name).queryString
        query shouldEqual s"$name ASC"
      }
    }
  }

  it should "serialise a DESC operator query" in {
    forAll(minSuccessful(300)) { (name: String) =>
      whenever (name.length > 0) {
        val query = MySQLQueryBuilder.desc(name).queryString
        query shouldEqual s"$name DESC"
      }
    }
  }

  it should "serialise a SET query" in {
    forAll(minSuccessful(300)) { (part: String, name: String) =>
      whenever (name.length > 0) {
        val query = MySQLQueryBuilder.set(SQLBuiltQuery(part), SQLBuiltQuery(name)).queryString
        query shouldEqual s"$part SET $name"
      }
    }
  }

  it should "serialise a LIKE operator query" in {
    forAll(minSuccessful(300)) { (part: String, name: String) =>
      whenever (name.length > 0) {
        val tested = part.trim
        val query = MySQLQueryBuilder.like(tested, name).queryString
        query shouldEqual s"$tested LIKE $name"
      }
    }
  }

  it should "serialise a NOT LIKE operator query" in {
    forAll(minSuccessful(300)) { (part: String, name: String) =>
      whenever (name.length > 0) {
        val query = MySQLQueryBuilder.notLike(part, name).queryString
        query shouldEqual s"$part NOT LIKE $name"
      }
    }
  }

  it should "correctly set multiple conditions" in {
    forAll(minSuccessful(300)) { (part: String, name: String) =>
      whenever (name.length > 0) {
        val query = MySQLQueryBuilder.andSet(SQLBuiltQuery(part), SQLBuiltQuery(name)).queryString
        query shouldEqual s"$part, $name"
      }
    }
  }

  it should "append an ENGINE clause" in {
    forAll(minSuccessful(300)) { (part: String, name: String) =>
      whenever (name.length > 0) {
        val query = MySQLQueryBuilder.engine(SQLBuiltQuery(part), name).queryString
        query shouldEqual s"$part ENGINE $name"
      }
    }
  }

  it should "serialise a BIN clause" in {
    forAll(minSuccessful(300)) { (part: String) =>
      whenever (part.length > 0) {
        val query = MySQLQueryBuilder.bin(part).queryString
        query shouldEqual s"BIN ($part)"
      }
    }
  }

  it should "serialise a CHAR_LENGTH clause" in {
    forAll(minSuccessful(300)) { (part: String) =>
      whenever (part.length > 0) {
        val query = MySQLQueryBuilder.charLength(part).queryString
        query shouldEqual s"CHAR_LENGTH ($part)"
      }
    }
  }

  it should "serialise a CHARACTER_LENGTH clause" in {
    forAll(minSuccessful(300)) { (part: String) =>
      whenever (part.length > 0) {
        val query = MySQLQueryBuilder.characterLength(part).queryString
        query shouldEqual s"CHARACTER_LENGTH ($part)"
      }
    }
  }

  it should "serialise a BIT_LENGTH clause" in {
    forAll(minSuccessful(300)) { (part: String) =>
      whenever (part.length > 0) {
        val query = MySQLQueryBuilder.bitLength(part).queryString
        query shouldEqual s"BIT_LENGTH ($part)"
      }
    }
  }

  it should "serialise a ASCII clause" in {
    forAll(minSuccessful(300)) { (part: String) =>
      whenever (part.length > 0) {
        val query = MySQLQueryBuilder.ascii(part).queryString
        query shouldEqual s"ASCII ($part)"
      }
    }
  }

  it should "serialise an EXISTS clause" in {
    forAll(minSuccessful(300)) { (part: String) =>
      whenever (part.length > 0) {
        val query = MySQLQueryBuilder.exists(SQLBuiltQuery(part)).queryString
        query shouldEqual s"EXISTS ($part)"
      }
    }
  }

  it should "serialise a NOT EXISTS clause" in {
    forAll(minSuccessful(300)) { (part: String) =>
      whenever (part.length > 0) {
        val query = MySQLQueryBuilder.notExists(SQLBuiltQuery(part)).queryString
        query shouldEqual s"NOT EXISTS ($part)"
      }
    }
  }

  it should "serialise a ON clause" in {
    forAll(minSuccessful(300)) { (part: String, value: String) =>
      whenever (!part.isEmpty && !value.isEmpty) {
        val query = MySQLQueryBuilder.on(SQLBuiltQuery(part), SQLBuiltQuery(value)).queryString
        query shouldEqual s"$part ON $value"
      }
    }
  }

  it should "serialise a BETWEEN clause" in {
    forAll(minSuccessful(300)) { (part: String, value: String) =>
      whenever (!part.isEmpty && !value.isEmpty) {
        val query = MySQLQueryBuilder.between(part, value).queryString
        query shouldEqual s"$part BETWEEN $value"
      }
    }
  }

  it should "serialise a NOT BETWEEN clause" in {
    forAll(minSuccessful(300)) { (part: String, value: String) =>
      whenever (!part.isEmpty && !value.isEmpty) {
        val query = MySQLQueryBuilder.notBetween(part, value).queryString
        query shouldEqual s"$part NOT BETWEEN $value"
      }
    }
  }
}

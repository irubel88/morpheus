/*
 * Copyright 2015 websudos ltd.
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

package com.websudos.morpheus.mysql

import com.websudos.morpheus.mysql.tables.{TestEnumeration, EnumerationRecord, BasicRecord}
import com.outworkers.util.testing._

package object db {

  implicit object BasicRecordSample extends Sample[BasicRecord] {
    def sample: BasicRecord = {
      BasicRecord(
        gen[String],
        gen[Int]
      )
    }
  }

  implicit object EnumerationRecordSamplers extends Sample[EnumerationRecord] {
    def sample: EnumerationRecord = {
      EnumerationRecord(
        id = gen[Int],
        enum = oneOf(TestEnumeration)
      )
    }
  }
}

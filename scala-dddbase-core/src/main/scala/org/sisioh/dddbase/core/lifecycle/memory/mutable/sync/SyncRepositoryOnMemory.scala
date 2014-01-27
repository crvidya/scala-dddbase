/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
 * Copyright 2011 Sisioh Project and others. (http://www.sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.memory.sync.{SyncRepositoryOnMemory => SROM}
import org.sisioh.dddbase.core.lifecycle.sync.SyncResultWithEntity
import org.sisioh.dddbase.core.model.{Identifier, EntityCloneable, Entity}
import scala.util.Try


/**
 * オンメモリで動作する可変リポジトリの実装。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemory
[ID <: Identifier[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SROM[ID, E] {

  /**
   * 内部で利用されるオンメモリリポジトリ
   */
  protected var core: SROM[ID, E] =
    new org.sisioh.dddbase.core.lifecycle.memory.sync.GenericSyncRepositoryOnMemory[ID, E]()

  override def equals(obj: Any) = obj match {
    case that: SyncRepositoryOnMemory[_, _] =>
      this.core == that.core
    case _ => false
  }

  override def hashCode = 31 * core.##

  def store(entity: E)(implicit ctx: Ctx): Try[SyncResultWithEntity[This, ID, E]] = {
    core.store(entity).map {
      resultWithEntity =>
        core = resultWithEntity.result.asInstanceOf[SROM[ID, E]]
        SyncResultWithEntity(this.asInstanceOf[This], resultWithEntity.entity)
    }
  }

  def deleteBy(identifier: ID)(implicit ctx: Ctx): Try[SyncResultWithEntity[This, ID, E]] = {
    core.deleteBy(identifier).map {
      result =>
        SyncResultWithEntity(this.asInstanceOf[This], result.entity)
    }
  }

  def iterator: Iterator[E] = core.iterator

  def resolveBy(identifier: ID)(implicit ctx: Ctx): Try[E] = core.resolveBy(identifier)

}
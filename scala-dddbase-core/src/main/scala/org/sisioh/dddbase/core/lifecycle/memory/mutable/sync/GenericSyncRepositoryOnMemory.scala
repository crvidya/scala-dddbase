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

import org.sisioh.dddbase.core.model.{Identity, EntityCloneable, Entity}
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

/**
 * 汎用的な同期型オンメモリ可変リポジトリ。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
class GenericSyncRepositoryOnMemory[ID <: Identity[_], E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemorySupport[ID, E] {

  type This = GenericSyncRepositoryOnMemory[ID, E]

}

/**
 * コンパニオンオブジェクト。
 */
object GenericSyncRepositoryOnMemory {

  object Implicits {

    implicit val defaultEntityIOContext = SyncEntityIOContext

  }

  /**
   * ファクトリメソッド。
   *
   * @tparam ID 識別子の型
   * @tparam T エンティティの型
   * @return [[org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.GenericSyncRepositoryOnMemory]]
   */
  def apply[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T] with Ordered[T]]() =
    new GenericSyncRepositoryOnMemory[ID, T]

}
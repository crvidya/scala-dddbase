package org.sisioh.dddbase.core.lifecycle.memory.sync

import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model._
import scala.Some
import scala.util._
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.sync.SyncRepositoryOnMemorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupportByOption
[ID <: Identity[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemorySupport[ID, E]
  with SyncEntityReadableByOption[ID, E] {

  override def resolveOption(identity: ID)(implicit ctx: EntityIOContext[Try]) = synchronized {
    containsByIdentity(identity).map {
      result =>
        if (result) {
          Some(entities(identity).clone)
        } else {
          None
        }
    }.getOrElse(None)
  }

}

package org.sisioh.dddbase.core.lifecycle.memory.mutable.sync

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntityNotFoundException}
import org.sisioh.dddbase.core.lifecycle.sync._
import org.sisioh.dddbase.core.model._
import scala.util._

/**
 * [[org.sisioh.dddbase.core.lifecycle.memory.mutable.sync.SyncRepositoryOnMemorySupport]]にOption型のサポートを追加するトレイト。
 *
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 */
trait SyncRepositoryOnMemorySupportByOption
[ID <: Identifier[_],
E <: Entity[ID] with EntityCloneable[ID, E] with Ordered[E]]
  extends SyncRepositoryOnMemorySupport[ID, E]
  with SyncEntityReadableByOption[ID, E] {

  def resolveOption(identifier: ID)(implicit ctx: EntityIOContext[Try]): Option[E] = synchronized {
    resolveBy(identifier).map(Some(_)).recoverWith {
      case ex: EntityNotFoundException =>
        Success(None)
    }.getOrElse(None)
  }

}

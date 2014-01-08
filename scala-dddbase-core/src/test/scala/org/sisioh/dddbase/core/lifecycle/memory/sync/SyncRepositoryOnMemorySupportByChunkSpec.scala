package org.sisioh.dddbase.core.lifecycle.memory.sync

import org.sisioh.dddbase.core.model._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityIOContext

class SyncRepositoryOnMemorySupportByChunkSpec extends Specification with Mockito {

  class EntityImpl(val identifier: Identifier[Int])
    extends Entity[Identifier[Int]]
    with EntityCloneable[Identifier[Int], EntityImpl]
    with Ordered[EntityImpl] {

    def compare(that: SyncRepositoryOnMemorySupportByChunkSpec.this.type#EntityImpl): Int = {
      this.identifier.value.compareTo(that.identifier.value)
    }

  }

  class TestSyncRepository
    extends SyncRepositoryOnMemorySupport[Identifier[Int], EntityImpl]()
    with SyncRepositoryOnMemorySupportByChunk[Identifier[Int], EntityImpl] {
    type This = TestSyncRepository
  }

  implicit val ctx = SyncEntityIOContext

  "The repository" should {
    "have stored entities" in {

      var repository = new TestSyncRepository

      for (i <- 1 to 10) {
        val entity = new EntityImpl(Identifier[Int](i))
        repository = repository.store(entity).get.result
      }

      val chunk = repository.resolveChunk(1, 5).get

      chunk.index must_== 1
      chunk.entities.size must_== 5
      chunk.entities(0).identifier.value must_== 6
      chunk.entities(1).identifier.value must_== 7
      chunk.entities(2).identifier.value must_== 8
      chunk.entities(3).identifier.value must_== 9
      chunk.entities(4).identifier.value must_== 10
    }
  }


}

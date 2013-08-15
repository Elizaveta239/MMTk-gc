/*
 *  This file is part of the Jikes RVM project (http://jikesrvm.org).
 *
 *  This file is licensed to You under the Eclipse Public License (EPL);
 *  You may not use this file except in compliance with the License. You
 *  may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/eclipse-1.0.php
 *
 *  See the COPYRIGHT.txt file distributed with this work for information
 *  regarding copyright ownership.
 */
package org.mmtk.plan.tutorial;

import org.mmtk.plan.StopTheWorldMutator;
import org.mmtk.plan.marksweep.MS;
import org.mmtk.policy.CopyLocal;
import org.mmtk.policy.MarkSweepLocal;
import org.mmtk.policy.Space;
import org.mmtk.utility.alloc.Allocator;
import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

/**
 * This class implements <i>per-mutator thread</i> behavior and state
 * for the <i>NoGC</i> plan, which simply allocates (without ever collecting
 * until the available space is exhausted.<p>
 *
 * Specifically, this class defines <i>NoGC</i> mutator-time allocation
 * through a bump pointer (<code>def</code>) and includes stubs for
 * per-mutator thread collection semantics (since there is no collection
 * in this plan, these remain just stubs).
 *
 * @see NoGC
 * @see NoGCCollector
 * @see org.mmtk.plan.StopTheWorldMutator
 * @see org.mmtk.plan.MutatorContext
 */
@Uninterruptible
public class TutorialMutator extends StopTheWorldMutator {

  /************************************************************************
   * Instance fields
   */

  /**
   *
   */
    private final MarkSweepLocal ms = new MarkSweepLocal(Tutorial.msSpace);
	private final CopyLocal nursery = new CopyLocal(Tutorial.nurserySpace);

  /****************************************************************************
   * Mutator-time allocation
   */

  /**
   * {@inheritDoc}
   */
  @Inline
  @Override
  public Address alloc(int bytes, int align, int offset, int allocator, int site) {
    if (allocator == Tutorial.ALLOC_DEFAULT) {
      return nursery.alloc(bytes, align, offset);
    }
    return super.alloc(bytes, align, offset, allocator, site);
  }

  @Inline
  @Override
  public void postAlloc(ObjectReference ref, ObjectReference typeRef,
      int bytes, int allocator) {
	  /*
	  if (allocator == Tutorial.ALLOC_DEFAULT) {
    	Tutorial.msSpace.postAlloc(ref);
    } else {
      super.postAlloc(ref, typeRef, bytes, allocator);
    }
    */
	  if (allocator != Tutorial.ALLOC_DEFAULT) {
		  super.postAlloc(ref, typeRef, bytes, allocator);
	  }
  }

  @Override
  public Allocator getAllocatorFromSpace(Space space) {
    if (space == Tutorial.nurserySpace) return nursery;
    return super.getAllocatorFromSpace(space);
  }


  /****************************************************************************
   * Collection
   */

  /**
   * {@inheritDoc}
   */
  @Inline
  @Override
  public final void collectionPhase(short phaseId, boolean primary) {
    //VM.assertions.fail("GC Triggered in NoGC Plan.");
	  if (phaseId == MS.PREPARE) {
		  super.collectionPhase(phaseId, primary);
		  //ms.prepare();
		  nursery.reset();
		  return;
		}

	  if (phaseId == MS.RELEASE) {
		  //ms.release();
		  super.collectionPhase(phaseId, primary);
		  return;
		}
	  
     super.collectionPhase(phaseId, primary);
  }
}

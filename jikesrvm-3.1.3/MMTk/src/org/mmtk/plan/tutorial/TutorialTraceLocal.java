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

import org.mmtk.plan.TraceLocal;
import org.mmtk.plan.Trace;
import org.mmtk.policy.Space;

import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

/**
 * This class implements the thread-local core functionality for a transitive
 * closure over the heap graph.
 */
@Uninterruptible
public final class TutorialTraceLocal extends TraceLocal {

  /**
   * Constructor
   */
  public TutorialTraceLocal(Trace trace) {
	  super(Tutorial.SCAN_MARK, trace);
  }


  /****************************************************************************
   * Externally visible Object processing and tracing
   */

  /**
   * {@inheritDoc}
   */
  public boolean isLive(ObjectReference object) {
    if (object.isNull()) return false;
    if (Space.isInSpace(Tutorial.MARK_SWEEP, object)) {
      return Tutorial.msSpace.isLive(object);
    }
    if (Space.isInSpace(Tutorial.NURSERY, object)) {
    	  return Tutorial.nurserySpace.isLive(object);
    }
    return super.isLive(object);
  }

  @Inline
  @Override
  public ObjectReference traceObject(ObjectReference object) {
    if (object.isNull()) return object;
    if (Space.isInSpace(Tutorial.MARK_SWEEP, object)) {
      return Tutorial.msSpace.traceObject(this, object);
    }
    if (Space.isInSpace(Tutorial.NURSERY, object)) {
    	  return Tutorial.nurserySpace.traceObject(this, object, Tutorial.ALLOC_DEFAULT);
    }
    return super.traceObject(object);
  }
  /*
  @Override
  public ObjectReference precopyObject(ObjectReference object) {
    if (object.isNull()) return object;
    else if (Space.isInSpace(Tutorial.NURSERY, object))
      return Tutorial.nurserySpace.traceObject(this, object, Tutorial.ALLOC_DEFAULT);
    else
      return object;
  }
  */
  
  @Override
  public boolean willNotMoveInCurrentCollection(ObjectReference object) {
    return !Space.isInSpace(Tutorial.NURSERY, object);
  }
}

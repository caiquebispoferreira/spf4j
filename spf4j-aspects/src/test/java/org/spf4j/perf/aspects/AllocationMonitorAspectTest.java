/*
 * Copyright (c) 2001-2017, Zoltan Farkas All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Additionally licensed with:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spf4j.perf.aspects;

import com.google.common.base.Strings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.spf4j.perf.impl.RecorderFactory;
import java.io.IOException;
import java.util.Collection;
import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spf4j.perf.MeasurementStoreQuery;
import org.spf4j.perf.io.OpenFilesSampler;
import org.spf4j.perf.memory.MemoryUsageSampler;
import org.spf4j.perf.memory.TestClass;

/**
 *
 * @author zoly
 */
@SuppressFBWarnings("MDM_THREAD_YIELD")
public final class AllocationMonitorAspectTest {

  private static final Logger LOG = LoggerFactory.getLogger(AllocationMonitorAspectTest.class);

  private static void testAllocInStaticContext() throws InterruptedException {
    for (int i = 0; i < 1000; i++) {
      LOG.debug("S{}{}", i, Strings.repeat("A", i % 2 * 2));
      if (i % 100 == 0) {
        Thread.sleep(100);
      }
    }
  }

  /**
   * Test of afterAllocation method, of class AllocationMonitorAspect.
   */
  @Test
  public void testAfterAllocation() throws InterruptedException, IOException {
    System.setProperty("spf4j.perf.allocations.sampleTimeMillis", "1000");
    MemoryUsageSampler.start(500);
    OpenFilesSampler.start(500, 512, 1000, false);
    for (int i = 0; i < 1000; i++) {
      LOG.debug("T{}", i);
      if (i % 100 == 0) {
        Thread.sleep(500);
      }
    }
    testAllocInStaticContext();
    TestClass.testAllocInStaticContext();
    RecorderFactory.MEASUREMENT_STORE.flush();
    MeasurementStoreQuery query = RecorderFactory.MEASUREMENT_STORE.query();
    Collection<Schema> measurements = query.getMeasurements((x) -> "heap_used".equals(x));
    Assert.assertEquals(1, measurements.size());
    MemoryUsageSampler.stop();
    OpenFilesSampler.stop();
  }
}

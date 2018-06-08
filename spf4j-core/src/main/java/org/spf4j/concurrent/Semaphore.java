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
package org.spf4j.concurrent;

import java.util.concurrent.TimeUnit;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

/**
 * A interface that abstracts a semaphore.
 * @author zoly
 */
@ParametersAreNonnullByDefault
@ThreadSafe
public interface Semaphore extends PermitSupplier {

  /**
   * release 1 permit.
   */
  default void release() {
    release(1);
  }

  /**
   * release a number of permits.
   * @param nrPermits  the number of permits to release.
   */
  void release(int nrPermits);


  static Semaphore from(final PermitSupplier supplier) {
    return new Semaphore() {
      @Override
      public void release(final int nrPermits) {
        // nothing to release.
      }

      @Override
      public boolean tryAcquire(final int nrPermits, final long timeout, final TimeUnit unit)
              throws InterruptedException {
        return supplier.tryAcquire(nrPermits, timeout, unit);
      }
    };
  }

}

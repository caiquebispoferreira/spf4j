/*
 * Copyright (c) 2001, Zoltan Farkas All Rights Reserved.
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
 */
package org.spf4j.zel.instr;

import org.spf4j.zel.vm.AssignableValue;
import org.spf4j.zel.vm.ExecutionContext;


/**
 * @author zoly
 */
public final class LODAX extends Instruction {

    private static final long serialVersionUID = 1257172216541960034L;

    private LODAX() {
    }

    /**
     * The instruction microcode
     * @param context ExecutionContext
     */
    @Override
    public void execute(final ExecutionContext context) {
        final Object ref = context.code.get(++context.ip);
        context.push(new AssignableValue() {

            @Override
            public void assign(Object object) {
                context.memory.put(ref, object);
            }

            @Override
            public Object get() {
                return context.memory.get(ref);
            }
        });
        context.ip++;
    }
    /**
     * instance
     */
    public static final Instruction INSTANCE = new LODAX();
}

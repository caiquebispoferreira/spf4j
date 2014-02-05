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
package org.spf4j.zel.instr.var;

import java.util.List;
import org.spf4j.zel.instr.Instruction;
import org.spf4j.zel.vm.EndParamMarker;
import org.spf4j.zel.vm.ExecutionContext;
import org.spf4j.zel.vm.VMExecutor;



/**
 *
 * @author zoly
 */
public final class DECODE extends Instruction {

    private static final long serialVersionUID = 625079099199504734L;

    private DECODE() {
    }

    @Override
    public void execute(final ExecutionContext context)
            throws VMExecutor.SuspendedException {
        Object param;
        Object result = null;

        List<Object> params = context.popSyncStackValsUntil(EndParamMarker.INSTANCE);
                
        int l = params.size();
        final Object expr = params.get(l - 1);
        for (int i = l - 2; i >= 0; i -= 2) {
            param = params.get(i);
            if (i - 1 < 0) {
                result = param;
                break;
            } else if (expr.equals(param)) {
                result = params.get(i - 1);
                break;
            }
        }
        context.push(result);
        context.ip++;
    }
    /**
     * instance
     */
    public static final Instruction INSTANCE = new DECODE();
}

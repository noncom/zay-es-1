/*
 * $Id$
 *
 * Copyright (c) 2013 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package trap.game;

import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *  Sorts through combat targets, calculates initiative, and
 *  then deals out the appropriate attack entities.
 *  
 *  @author    Paul Speed
 */
public class CombatInitiativeService implements Service {
 
    static Logger log = LoggerFactory.getLogger(CombatInitiativeService.class); 
 
    private EntityData ed;    
    private EntitySet meleeAttackers;
       
    public CombatInitiativeService() {
    }

    public void initialize( GameSystems systems ) {
        this.ed = systems.getEntityData();
        meleeAttackers = ed.getEntities(MeleeTarget.class);
    }

    public void update( long gameTime ) {
        meleeAttackers.applyChanges();
        if( !meleeAttackers.isEmpty() ) {
        
            // Temporarily just deal out hitpoint changes directly.                
            for( Entity e : meleeAttackers ) {
                MeleeTarget target = e.get(MeleeTarget.class);

System.out.println( "Creating health change for time:" + target.getTime() ); 
                EntityFactories.createBuff(target.getTime(), target.getTarget(), new HealthChange(-1)); 
                ed.removeComponent(e.getId(), MeleeTarget.class);                
            }
        }
    }

    public void terminate( GameSystems systems ) {
        meleeAttackers.release();
    }
    
}

package relay;

// -----( B2B Java Code Template v1.2
// -----( CREATED: Thu Aug 29 09:26:46 EST 2002
// -----( ON-HOST: fbs3

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<B2B-START-IMPORTS>> ---
import java.util.*;
import com.wm.app.b2b.server.ServiceThread;
// --- <<B2B-END-IMPORTS>> ---

public final class util
{
	// ---( internal utility methods )---

	final static util _instance = new util();

	static util _newInstance() { return new util(); }

	static util _cast(Object o) { return (util)o; }

	// ---( server methods )---




	public static final void collectGarbage (IData pipeline)
        throws ServiceException
	{
		// --- <<B2B-START(collectGarbage)>> ---
		// @sigtype java 3.5
		/* Runs the garbage collector. Calling the gc method suggests that the Java Virtual Machine expend 
		 * effort toward recycling unused objects in order to make the memory they currently occupy available
		 * for quick reuse. When control returns from the method call, the Java Virtual Machine has made a 
		 * best effort to reclaim space from all discarded objects. The call System.gc() is effectively 
		 * equivalent to the call: Runtime.getRuntime().gc() #
		 *  #-Taken directly from the Java 1.2.2 API Documentation, Copyright 1993-1999 Sun Microsystems, Inc.
		 */
		System.gc();
		// --- <<B2B-END>> ---

                
	}



	public static final void spawn (IData pipeline)
        throws ServiceException
	{
		// --- <<B2B-START(spawn)>> ---
		// @sigtype java 3.5
		// [i] field:0:required service
		// [i] field:0:required folder
		// [i] record:0:optional input
		// [o] object:0:required runningService
		// [o] field:0:required successFlag
	//define input variables 
	IDataCursor idcPipeline = pipeline.getCursor();
	String service, folder = null;
	IData input;

	//Output Variables 
	String successFlag = "false";

	// Check to see if the filename object is in the pipeline
	if (idcPipeline.first("service"))
	{
			//get the filename out of the pipeline						
			service = (String)idcPipeline.getValue();	
	}
	//if it is not in the pipeline, then handle the error
	else
	{
		System.out.println("Error executing sample.threads.spawnThread: required parameter 'service' missing.");
		successFlag="false";
			
		//insert the successFlag into the pipeline
		idcPipeline.insertAfter("successFlag", successFlag);	

		//Always destroy cursurs that you created
		idcPipeline.destroy();	

		return;
	}

	if (idcPipeline.first("folder"))
	{
			//get the folder out of the pipeline						
			folder = (String)idcPipeline.getValue();	

	}
	//if it is not in the pipeline, then handle the error
	else
	{
		System.out.println("Error executing sample.threads.spawnThread: required parameter 'folder' missing.");
		successFlag="false";
			
		//insert the successFlag into the pipeline
		idcPipeline.insertAfter("successFlag", successFlag);	

		//Always destroy cursurs that you created
		idcPipeline.destroy();	

		return;
	}

	if (idcPipeline.first("input"))
	{
			//get the filename out of the pipeline						
			input = (IData)idcPipeline.getValue();	

	}
	//if it is not in the pipeline, then get the value from the pipeline
	else
	{
			input = pipeline;
	}

	//Delete the runningService object from the pipeline if it exists 
	if(idcPipeline.first("runningService"))
			idcPipeline.delete();


	
	// Spawn the new thread cloning the pipeline.  This needs to be done to pass
	// a copy of the pipeline as it is always changing in memory.
	ServiceThread st = Service.doThreadInvoke(folder, service, IDataUtil.clone(input));
	
	//Set the successFlag	
	successFlag="true";

	//insert the runningService into the pipeline
	idcPipeline.insertAfter("runningService", st);

	//insert the successFlag into the pipeline
	idcPipeline.insertAfter("successFlag", successFlag);	
		// --- <<B2B-END>> ---

                
	}
}


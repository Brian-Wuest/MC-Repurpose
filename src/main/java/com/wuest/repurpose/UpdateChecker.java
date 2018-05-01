package com.wuest.repurpose;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import scala.actors.Debug;

/**
 * 
 * @author WuestMan
 *
 */
public class UpdateChecker
{
	/**
	 * Determines if a the message is shown to the user when they log into a world.
	 */
	public static boolean showMessage = false;
	
	/**
	 * The message to show to the user when they log into a world.
	 */
	public static String messageToShow = "";
	
	/**
	 * Checks the current version against the git-hub version.
	 */
	public static void checkVersion()
	{
		// Pull the repository information.
		ModContainer prefabMod = null;
		
		for (ModContainer modContainer : Loader.instance().getModList())
		{
			if (modContainer.getName().toLowerCase().equals(Repurpose.MODID.toLowerCase()))
			{
				prefabMod = modContainer;
				break;
			}
		}
		
		if (prefabMod != null)
		{
			CheckResult result = ForgeVersion.getResult(prefabMod);
			
			if (result != null && result.status == Status.OUTDATED)
			{
				// Current version is out dated, show the message when the user is logged in.
				UpdateChecker.messageToShow = "[Repurpose] There is a new version available! New Version: [" + result.target.toString() + "] Your Version: ["
						+ Repurpose.VERSION + "]";
				
				UpdateChecker.showMessage = true;
			}
		}
	}
}
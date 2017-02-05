package com.wuest.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;

import net.minecraft.util.text.TextComponentString;
import scala.actors.Debug;

public class UpdateChecker
{
	private static ModInfo modInfo = null;
	public static boolean showMessage = false;
	public static String messageToShow = "";
	
	public static void checkVersion()
	{
		UpdateChecker.getRepositoryVersion();
		
		if (UpdateChecker.modInfo != null)
		{
			UpdateChecker.messageToShow = "[Wuest-Utilities] There is a new version available! [New Version: " 
					+ UpdateChecker.modInfo.version + "] [Your Version: " + WuestUtilities.VERSION + "]";
			
			if (!WuestUtilities.VERSION.equals(modInfo.version))
			{
				// Old version, show a message;
				UpdateChecker.showMessage = true;
				
				System.out.println(UpdateChecker.messageToShow);
			}
			else
			{
				System.out.println("[Wuest-Utilities] - This is the latest version.");
			}
		}
		else
		{
			UpdateChecker.messageToShow = "Unable to retreive version information, something may be wrong with your network connection.";
			UpdateChecker.showMessage = true;
			System.out.println("[Wuest-Utilities] - " + UpdateChecker.messageToShow);
		}
	}
	
	private static void getRepositoryVersion()
	{
		try
		{
			System.out.println("[Wuest-Utilities] - Checking for latest version.");
			URL url = null;
			
			if (WuestUtilities.isDebug)
			{
				url = new File("..\\src\\main\\resources\\mcmod.info").toURI().toURL();
			}
			else
			{
				url = new URL("https://raw.githubusercontent.com/Brian-Wuest/MC-WuestUtilities/MC-1.11/src/main/resources/mcmod.info");
			}
			
			Gson file = new Gson();
			InputStreamReader stream = new InputStreamReader(url.openStream(), "UTF-8");
			ModInfo[] info = file.fromJson(stream, ModInfo[].class);
			stream.close();
			UpdateChecker.modInfo = info[0];
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
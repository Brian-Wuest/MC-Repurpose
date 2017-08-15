package com.wuest.repurpose;

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
			UpdateChecker.messageToShow = "[Repurpose] There is a new version available! [New Version: " 
					+ UpdateChecker.modInfo.version + "] [Your Version: " + Repurpose.VERSION + "]";
			
			if (!Repurpose.VERSION.equals(modInfo.version))
			{
				// Old version, show a message;
				UpdateChecker.showMessage = true;
				
				System.out.println(UpdateChecker.messageToShow);
			}
			else
			{
				System.out.println("[Repurpose] - This is the latest version.");
			}
		}
		else
		{
			UpdateChecker.messageToShow = "Unable to retreive version information, something may be wrong with your network connection.";
			UpdateChecker.showMessage = true;
			System.out.println("[Repurpose] - " + UpdateChecker.messageToShow);
		}
	}
	
	private static void getRepositoryVersion()
	{
		try
		{
			System.out.println("[Repurpose] - Checking for latest version.");
			URL url = null;
			
			if (Repurpose.isDebug)
			{
				url = new File("..\\src\\main\\resources\\mcmod.info").toURI().toURL();
			}
			else
			{
				url = new URL("https://raw.githubusercontent.com/Brian-Wuest/MC-Repurpose/MC-1.12/src/main/resources/mcmod.info");
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
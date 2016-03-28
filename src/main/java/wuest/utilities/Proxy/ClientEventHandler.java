package wuest.utilities.Proxy;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wuest.utilities.WuestUtilities;

/**
 * This class is used to handle client side only events.
 * @author Brian
 *
 */
public class ClientEventHandler extends Gui
{
	public static LocalDateTime bedCompassTime;
	public static BlockPos bedLocation;
	private static final int BUFF_ICON_SIZE = 18;
	private static final int BUFF_ICON_SPACING = BUFF_ICON_SIZE + 5; // 2 pixels between buff icons
	private static final int BUFF_ICON_BASE_U_OFFSET = 0;
	private static final int BUFF_ICON_BASE_V_OFFSET = 198;
	private static final int BUFF_ICONS_OFFSET = 8;
	
	//
	// This event is called by GuiIngameForge during each frame by
	// GuiIngameForge.pre() and GuiIngameForce.post().
	//
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent event)
	{
		// We draw after the ExperienceBar has drawn.  The event raised by GuiIngameForge.pre()
		// will return true from isCancelable.  If you call event.setCanceled(true) in
		// that case, the portion of rendering which this event represents will be canceled.
		// We want to draw *after* the experience bar is drawn, so we make sure isCancelable() returns
		// false and that the eventType represents the ExperienceBar event.
		if(event.isCancelable() || event.getType() != ElementType.EXPERIENCE)
		{      
			return;
		}
		
		if (WuestUtilities.proxy.proxyConfiguration.enablePotionEffectOverlay)
		{
			this.ShowPotionEffects();
		}
		
		this.ShowPlayerBed(event);
	}

	private void ShowPotionEffects()
	{
		Minecraft mc = Minecraft.getMinecraft();

		// Starting position for the buff bar - 2 pixels from the top left corner.
		int xPos = 2;
		int yPos = 2;
		Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();
		int potionCount = 0;
		
		for (PotionEffect potionEffect : collection) 
		{
			xPos += BUFF_ICON_SPACING;
			Potion potion = potionEffect.getPotion();

			if (potion.hasStatusIcon())
			{
				int iconIndex = potion.getStatusIconIndex();

				mc.renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/gui/container/inventory.png"));

				this.drawTexturedModalRect(
						xPos, yPos, 
						BUFF_ICON_BASE_U_OFFSET + iconIndex % BUFF_ICONS_OFFSET * BUFF_ICON_SIZE, BUFF_ICON_BASE_V_OFFSET + iconIndex / BUFF_ICONS_OFFSET * BUFF_ICON_SIZE,
						BUFF_ICON_SIZE, 
						BUFF_ICON_SIZE);

				// Draw the effect duration.
				this.drawString(mc.fontRendererObj, potion.getPotionDurationString(potionEffect, 1), xPos, yPos + 20, Color.WHITE.getRGB());
				
				if (potionCount > 3)
				{
					// Make a new row for these potions.
					yPos = yPos + 30;
					xPos = 2;
					potionCount = 0;
				}
				
				potionCount++;
			}       
		}
	}
	
	private void ShowPlayerBed(RenderGameOverlayEvent event)
	{
		if (ClientEventHandler.bedCompassTime != null)
		{
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.thePlayer;
			long timeBetween = java.time.temporal.ChronoUnit.SECONDS.between(ClientEventHandler.bedCompassTime, LocalDateTime.now()); 
			
			// Do drawing logic here.
			int x = event.getResolution().getScaledWidth() / 4;
			int y = event.getResolution().getScaledHeight() / 2 - 23;
			BlockPos playerPosition = player.getPosition();
			
			if (ClientEventHandler.bedLocation != null)
			{
				// If yOffset is positive, the player is higher than the bed, if it's negative the player is lower than the bed.
				int yOffSet = playerPosition.getY() - ClientEventHandler.bedLocation.getY();
				
				// X = East/West, west being less than 0 and east being greater than 0.
				// If the offset is greater than 0 the player is east of the bed, otherwise the player is west of the bed.
				int xOffSet = playerPosition.getX() - ClientEventHandler.bedLocation.getX();
				
				// Z = North/South. North being less than 0 and south being greater than 0.
				// If the offset is greater than 0 then the player is south of the bed, otherwise the player is north of the bed.
				int zOffSet = playerPosition.getZ() - ClientEventHandler.bedLocation.getZ();
				
				this.drawString(mc.fontRendererObj, "Your bed is...", x, y, Color.WHITE.getRGB());
				
				y = y + 10;
				
				if (yOffSet > 0)
				{
					this.drawString(mc.fontRendererObj, ((Integer)yOffSet).toString() + " Block(s) Lower", x, y, (new Color(200, 117, 51).getRGB()));
				}
				else if (yOffSet < 0)
				{
					this.drawString(mc.fontRendererObj, ((Integer)Math.abs(yOffSet)).toString() + " Block(s) Higher", x, y, (new Color(52,221,221).getRGB()));
				}
				else
				{
					y = y - 10;
				}
				
				y = y + 10;
				
				if (xOffSet > 0)
				{
					this.drawString(mc.fontRendererObj, ((Integer)xOffSet).toString() + " Block(s) West", x, y, (new Color(207,83,0).getRGB()));
				}
				else if (xOffSet < 0)
				{
					this.drawString(mc.fontRendererObj, ((Integer)Math.abs(xOffSet)).toString() + " Block(s) East", x, y, (new Color(255,204,0).getRGB()));
				}
				else
				{
					y = y - 10;
				}
				
				y = y + 10;
				
				if (zOffSet > 0)
				{
					this.drawString(mc.fontRendererObj, ((Integer)zOffSet).toString() + " Block(s) North", x, y, (new Color(204,204,255).getRGB()));
				}
				else if (zOffSet < 0)
				{
					this.drawString(mc.fontRendererObj, ((Integer)Math.abs(zOffSet)).toString() + " Block(s) South", x, y, (new Color(91,194,54).getRGB()));
				}
				else
				{
					y = y - 10;
				}
				
				y = y + 10;
				
				if (xOffSet == 0 && yOffSet == 0 && zOffSet == 0)
				{
					this.drawString(mc.fontRendererObj, "Right next to you", x, y, Color.WHITE.getRGB());
				}
				else
				{
					this.drawString(mc.fontRendererObj, "Of you", x, y, Color.WHITE.getRGB());
				}
			}
			else
			{
				// Send a chat to the user that their bed was not found.
				mc.thePlayer.sendChatMessage("Bed Not Found");
				timeBetween = 99999999;
			}
			
			if (timeBetween > 8)
			{
				ClientEventHandler.bedCompassTime = null;
			}
		}
	}

}

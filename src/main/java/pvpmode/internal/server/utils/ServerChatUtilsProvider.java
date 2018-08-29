package pvpmode.internal.server.utils;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.*;
import pvpmode.api.server.utils.ServerChatUtils;
import pvpmode.internal.server.ServerProxy;

public class ServerChatUtilsProvider implements ServerChatUtils.Provider
{

    @Override
    public void postLocalChatMessages (ICommandSender recipient, EnumChatFormatting color, String... messages)
    {
        for (String message : messages)
        {
            for (String line : message.split ("\n"))
            {
                ChatComponentText root = null;
                for (String part : line.split ("§r"))
                {
                    ChatComponentText text = new ChatComponentText (part);
                    text.getChatStyle ().setColor (color);
                    if (root == null)
                    {
                        root = text;
                    }
                    else
                    {
                        root.appendSibling (text);
                    }
                }
                if (root != null)
                {
                    recipient.addChatMessage (root);
                }
            }
        }
    }

    @Override
    public void postLocalChatMessage (ICommandSender recipient, String firstPart, String secondPart,
        EnumChatFormatting firstColor, EnumChatFormatting secondColor)
    {
        ChatComponentText firstText = new ChatComponentText (firstPart);
        ChatComponentText secondText = new ChatComponentText (secondPart);

        firstText.getChatStyle ().setColor (firstColor);
        secondText.getChatStyle ().setColor (secondColor);

        recipient.addChatMessage (firstText.appendSibling (secondText));
    }

    @Override
    public void postGlobalChatMessages (EnumChatFormatting color, String... messages)
    {
        for (String message : messages)
        {
            for (String line : message.split ("\n"))
            {
                ChatComponentText root = null;
                for (String part : line.split ("§r"))
                {
                    ChatComponentText text = new ChatComponentText (part);
                    text.getChatStyle ().setColor (color);
                    if (root == null)
                    {
                        ChatComponentText prefix = new ChatComponentText (
                            ServerProxy.prefixGlobalMessages ? ServerProxy.globalMessagePrefix : "");
                        prefix.appendSibling (text);
                        root = prefix;
                    }
                    else
                    {
                        root.appendSibling (text);
                    }
                }
                if (root != null)
                {
                    ServerProxy.cfg.sendChatMsg (root);
                }
            }
        }
    }

}

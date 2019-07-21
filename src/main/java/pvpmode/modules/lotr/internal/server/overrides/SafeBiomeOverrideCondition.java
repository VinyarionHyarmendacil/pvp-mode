package pvpmode.modules.lotr.internal.server.overrides;

import java.util.*;

import lotr.common.*;
import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.player.EntityPlayer;
import pvpmode.api.common.EnumPvPMode;
import pvpmode.api.common.overrides.EnumForcedPvPMode;
import pvpmode.modules.lotr.api.server.LOTRServerConstants;
import pvpmode.modules.lotr.internal.server.FactionEntry;

public class SafeBiomeOverrideCondition extends MiddleEarthBiomeOverrideCondition
{

    public SafeBiomeOverrideCondition (Map<Integer, Collection<FactionEntry>> configurationData)
    {
        super (configurationData);
    }

    @Override
    public int getPriority ()
    {
        return 200;
    }

    @Override
    protected EnumForcedPvPMode handleCondition (FactionEntry entry, EntityPlayer player)
    {
        if (entry.getEntryName ().equals (LOTRServerConstants.FACTION_ENTRY_WILDCARD))
            return EnumForcedPvPMode.OFF;
        else
        {
            LOTRPlayerData data = LOTRLevelData.getData (player);
            if (entry.getInvolvedFactions ().stream ().anyMatch (
                factionName ->
                {
                    LOTRFaction faction = LOTRFaction.forName (factionName);
                    return data.getAlignment (faction) > entry
                        .getAlignment () && (!entry.isPledgingRequired () || data.isPledgedTo (faction));
                }))
                return EnumForcedPvPMode.OFF;
        }
        return EnumForcedPvPMode.UNDEFINED;
    }

    @Override
    public String getForcedOverrideMessage (EntityPlayer player, EnumPvPMode forcedMode, boolean global)
    {
        return String.format (
            "PvP is now disabled for %s upon entering a safe biome",
            global ? player.getDisplayName () : "you");
    }

}

package com.feed_the_beast.ftbquests.integration.jei;

import com.feed_the_beast.ftbquests.client.ClientQuestFile;
import com.feed_the_beast.ftbquests.quest.Quest;
import com.feed_the_beast.ftbquests.quest.QuestChapter;

import java.util.ArrayList;

/**
 * @author LatvianModder
 */
public enum QuestRegistry
{
	INSTANCE;

	public final ArrayList<QuestWrapper> list = new ArrayList<>();

	@SuppressWarnings("deprecation")
	public void refresh()
	{
		if (FTBQuestsJEIIntegration.runtime != null && !list.isEmpty())
		{
			for (QuestWrapper wrapper : list)
			{
				FTBQuestsJEIIntegration.runtime.getRecipeRegistry().removeRecipe(wrapper, QuestCategory.UID);
			}
		}

		list.clear();

		if (ClientQuestFile.exists())
		{
			for (QuestChapter chapter : ClientQuestFile.INSTANCE.chapters)
			{
				for (Quest quest : chapter.quests)
				{
					if (!quest.rewards.isEmpty())
					{
						QuestWrapper wrapper = new QuestWrapper(quest);
						list.add(wrapper);
						FTBQuestsJEIIntegration.runtime.getRecipeRegistry().addRecipe(wrapper, QuestCategory.UID);
					}
				}
			}
		}
	}
}
package dev.ftb.mods.ftbquests.net;

import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.simple.BaseC2SMessage;
import me.shedaniel.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * @author LatvianModder
 */
public class SubmitTaskMessage extends BaseC2SMessage {
	private final long task;

	SubmitTaskMessage(FriendlyByteBuf buffer) {
		task = buffer.readLong();
	}

	public SubmitTaskMessage(long t) {
		task = t;
	}

	@Override
	public MessageType getType() {
		return FTBQuestsNetHandler.SUBMIT_TASK;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeLong(task);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		ServerPlayer player = (ServerPlayer) context.getPlayer();
		TeamData data = TeamData.get(player);

		if (data.isLocked()) {
			return;
		}

		Task t = data.file.getTask(task);

		if (t != null && data.canStartTasks(t.quest)) {
			((ServerQuestFile) data.file).currentPlayer = player;
			t.submitTask(data, player);
			((ServerQuestFile) data.file).currentPlayer = null;
		}
	}
}
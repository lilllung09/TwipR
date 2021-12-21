package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Permissions;
import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandHelpMsg extends DefaultCommand {

	@Override
	public void execCommand(CommandSender sender, String[] args) {
		if (args.length <= 1 ) {// twipr help
			TwipRMessage.runCmd("tellraw " + sender.getName() + " "
					+ "[" +
							"{\"text\":\"\\n\"},{\"text\":\"==========TwipR==========\\n\"}" +

							",{\"text\":\"/twipr help [sub] - 전체 또는 sub 명령어에 대한 설명을 보여줍니다.\\n\"}" +

							(sender.isOp() ?
									",{\"text\":\"/twipr reload - 플러그인 재시작하기\\n\"" +
									",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
									",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr reload\"}}" +

									",{\"text\":\"/twipr state - 스트리머 연결상태 보기\\n\"" +
									",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
									",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr state\"}}" +

									",{\"text\":\"/twipr test [true|false] - 트윕 테스트 후원 결과 적용\\n\"" +
									",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
									",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr test\"}}" +

									",{\"text\":\"/twipr help queue - 큐 형식 슬롯머신 결과 적용 관련 명령어 보기\\n\"" +
									",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
									",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr help queue\"}}"
									
									: "") +


							",{\"text\":\"/twipr help st - 스트리머 설정 관련 명령어 보기\\n\"" +
								",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
								",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr help st\"}}" +

					"]"
			, 0);

		} else if (args.length == 2) {// twipr help streamer | ...
			switch (args[1]) {
				case "st" :
					if (sender.hasPermission(Permissions.COMMANDS_ST_EDIT.getValue())) {
						TwipRMessage.runCmd("tellraw " + sender.getName() + " "
										+ "[" +
										"{\"text\":\"\\n\"}" +
										",{\"text\":\"==========TwipR==========\\n\"}" +

										",{\"text\":\"/twipr st add [minecraftID] : 스트리머로 등록합니다.\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 나를 추가\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr st add\"}" +
										"}" +

										",{\"text\":\"/twipr st del [minecraftID] : 스트리머 등록을 해제합니다.\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 나를 해제\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr st del\"}" +
										"}" +

										",{\"text\":\"/twipr st info [minecraftID] : 등록된 Twip 정보를 봅니다.\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 내 정보 보기\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr st info\"}" +
										"}" +

										",{\"text\":\"/twipr st connect [minecraftID] true | false : 자동 연결 사용 또는 미사용\\n\"}" +
										",{\"text\":\"/twipr st key [minecraftID] YOUR_KEY : Twip API key를 등록합니다.\\n\"}" +
										",{\"text\":\"/twipr st token [minecraftID] YOUR_TOKEN : Twip API token을 등록합니다.\\n\"}" +
										",{\"text\":\"/twipr st preset [minecraftID] PRESET : 특정 프리셋을 사용합니다.\\n\"}" +

										"]"
								, 0);
					} else {
						TwipRMessage.runCmd("tellraw " + sender.getName() + " "
										+ "[" +
										"{\"text\":\"\\n\"}" +
										",{\"text\":\"==========TwipR==========\\n\"}" +

										",{\"text\":\"/twipr st add : 자신을 스트리머로 등록합니다.\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr st add\"}" +
										"}" +

										",{\"text\":\"/twipr st del : 자신을 등록 해제합니다.\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr st del\"}" +
										"}" +

										",{\"text\":\"/twipr st info : 등록된 Twip 정보를 봅니다.\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr st info\"}" +
										"}" +


										",{\"text\":\"/twipr st connect true | false : 자동 연결 사용 또는 미사용\\n\"}" +
										",{\"text\":\"/twipr st key YOUR_KEY : Twip API key를 등록합니다.\\n\"}" +
										",{\"text\":\"/twipr st token YOUR_TOKEN : Twip API token을 등록합니다.\\n\"}" +
										",{\"text\":\"/twipr st preset PRESET : 특정 프리셋을 사용합니다.\\n\"}" +

										"]"
								, 0);
					}
					break;

				case "queue":
					if (sender.hasPermission(Permissions.COMMANDS_QUEUE.getValue())) {
						TwipRMessage.runCmd("tellraw " + sender.getName() + " "
										+ "[" +
										"{\"text\":\"\\n\"}" +
										",{\"text\":\"==========TwipR==========\\n\"}" +

										",{\"text\":\"/twipr queue start [sec] : sec 초 마다 룰렛 결과를 적용합니다.(기본값: 4초)\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 큐 시작\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr queue start\"}" +
										"}" +

										",{\"text\":\"/twipr queue pause : 큐가 일시정지 됩니다.\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 큐 일시정지\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr queue pause\"}" +
										"}" +

										",{\"text\":\"/twipr queue stop : 큐 형식이 중지 되고 결과가 바로 적용됩니다.\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 큐 중지\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr queue stop\"}" +
										"}" +

										",{\"text\":\"/twipr queue check : 각 스트리머의 큐 상태를 보여줍니다.(스트리머에게도 각자의 상태를 보여줍니다, 0개 제외)\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr queue check\"}" +
										"}" +

										",{\"text\":\"/twipr queue check o : 각 스트리머의 큐 상태를 자신만 봅니다.\\n\",\"bold\":false" +
										",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
										",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr queue check o\"}" +
										"}" +


										"]"
								, 0);
					}
					break;

				default:
					new CommandMissMatchArgs().execCommand(sender, args);
					break;

			}
		}
	}

	@Override
	public List<String> getSubCommands(CommandSender sender, String[] args) {
		List<String> list = new ArrayList<>();
		list.add("st");

		return super.getMatchingSubCommands(list, args[args.length-1]);
	}

}

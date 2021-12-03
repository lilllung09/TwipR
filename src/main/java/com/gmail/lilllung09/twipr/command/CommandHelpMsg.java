package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.command.CommandSender;

public class CommandHelpMsg implements DefaultCommand {

	@Override
	public void execCommand(CommandSender commandSender, String[] args) {


		if (args.length <= 1 ) {// twipr help
			TwipRMessage.runCmd("tellraw " + commandSender.getName() + " "
					+ "[" +
							"{\"text\":\"\\n\"},{\"text\":\"==========TwipR 3.0==========\\n\"}" +

							(commandSender.isOp() ?
									",{\"text\":\"/twipr reload - 플러그인 재시작하기\\n\"" +
									",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
									",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr reload\"}}" +

									",{\"text\":\"/twipr state - 스트리머 연결상태 보기\\n\"" +
									",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
									",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr state\"}}"
									
									: "") +

							",{\"text\":\"/twipr help [sub] - 전체 또는 sub 명령어에 대한 설명을 보여줍니다.\\n\"}" +


							",{\"text\":\"/twipr help st - 스트리머 설정 관련 명령어 보기\\n\"" +
								",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
								",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr help st\"}}" +

					"]"
			, 0);

		} else if (args.length == 2) {// twipr help streamer | ...
			switch (args[1]) {
				case "st" :
					if (commandSender.isOp()) {
						TwipRMessage.runCmd("tellraw " + commandSender.getName() + " "
										+ "[" +
										"{\"text\":\"\\n\"}" +
										",{\"text\":\"==========TwipR 3.0==========\\n\"}" +

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
						TwipRMessage.runCmd("tellraw " + commandSender.getName() + " "
										+ "[" +
										"{\"text\":\"\\n\"}" +
										",{\"text\":\"==========TwipR 3.0==========\\n\"}" +

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


				default:
					new CommandMissMatchArgs().execCommand(commandSender, args);
					break;

			}
		} else {
			new CommandMissMatchArgs().execCommand(commandSender, args);
		}
	}

}

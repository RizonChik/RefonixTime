package ru.refontstudio.refonixtime;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public final class RefonixTime extends JavaPlugin {

    private Map<String, Integer> cities = new HashMap<>();

    @Override
    public void onEnable() {
        cities.put("москва", 3);
        cities.put("екатеринбург", 5);
        cities.put("новосибирск", 7);
        cities.put("владивосток", 10);
        cities.put("спб", 3);
        cities.put("санкт-петербург", 3);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("time") || args.length == 0) {
            String usage = "§x§F§F§4§4§4§4§lИ§x§F§F§6§6§2§2§lс§x§F§F§8§8§0§0§lп§x§E§E§A§A§0§0§lо§x§C§C§C§C§0§0§lл§x§A§A§E§E§0§0§lь§x§8§8§F§F§0§0§lз§x§6§6§F§F§2§2§lо§x§4§4§F§F§4§4§lв§x§2§2§F§F§6§6§lа§x§0§0§F§F§8§8§lн§x§0§0§E§E§A§A§lи§x§0§0§C§C§C§C§lе§f: §b/time <город> §7или §b/time <город> <смещение>";
            sender.sendMessage(usage);
            return true;
        }

        String city = args[0].toLowerCase();
        String cityDisplay = city.substring(0, 1).toUpperCase() + city.substring(1);

        if (args.length > 1) {
            try {
                String offsetStr = args[1].replace("+", "");
                int offset = 3 + Integer.parseInt(offsetStr);
                cities.put(city, offset);
                showTime(sender, cityDisplay + " (МСК" + args[1] + ")", offset);
            } catch (Exception e) { //если чел тупит то:
                String error = "§x§F§F§0§0§0§0§lО§x§F§F§2§2§0§0§lш§x§F§F§4§4§0§0§lи§x§F§F§6§6§0§0§lб§x§F§F§8§8§0§0§lк§x§F§F§A§A§0§0§lа§f!";
                sender.sendMessage(error + " §7Укажите смещение от Москвы:");
                sender.sendMessage("§x§0§0§F§F§0§0§l+§x§2§2§F§F§2§2§l1 §7(на час вперед), §x§F§F§0§0§0§0§l-§x§F§F§2§2§2§2§l2 §7(на 2 часа назад)");
            }
        } else if (cities.containsKey(city)) {
            showTime(sender, cityDisplay, cities.get(city));
        } else {
            String notFound = "§x§0§0§8§8§F§5§lГ§x§0§0§8§4§F§3§lо§x§0§1§8§1§F§2§lр§x§0§1§7§D§F§0§lо§x§0§2§7§9§E§E§lд §x§0§3§7§2§E§B§lн§x§0§3§6§E§E§9§lе §x§0§4§6§7§E§5§lн§x§0§5§6§3§E§4§lа§x§0§5§6§0§E§2§lй§x§0§6§5§C§E§0§lд§x§0§6§5§8§D§E§lе§x§0§7§5§5§D§D§lн§x§0§7§5§1§D§B§l!";
            sender.sendMessage(notFound);

            String help = "§x§0§0§8§8§F§5§lП§x§0§1§8§0§F§1§lр§x§0§2§7§8§E§E§lи§x§0§3§7§0§E§A§lм§x§0§4§6§9§E§6§lе§x§0§5§6§1§E§2§lр§x§0§6§5§9§D§F§lы§x§0§7§5§1§D§B§l:";
            sender.sendMessage(help);
            sender.sendMessage("§x§0§0§F§F§8§8§l/time " + cityDisplay + " +1 §7(на час вперед от МСК)");
            sender.sendMessage("§x§0§0§F§F§8§8§l/time " + cityDisplay + " -2 §7(на 2 часа назад от МСК)");
        }

        return true;
    }

    private void showTime(CommandSender sender, String city, int offset) {
        String time = LocalDateTime.now(ZoneId.of("UTC")).plusHours(offset)
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        String timeGradient = "§fВремя";
        String cityGradient = "§x§F§F§D§D§0§0§l" + city.charAt(0);
        for (int i = 1; i < city.length(); i++) {
            cityGradient += "§x§F§F§" + Integer.toHexString(Math.max(0, 13-i)) + "§" + Integer.toHexString(Math.max(0, 13-i)) + "§0§0§l" + city.charAt(i);
        }

        sender.sendMessage(timeGradient + " §fв городе " + cityGradient + "§f: §x§0§0§F§F§0§0" + time);
    }
}
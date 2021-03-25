package me.alpho320.fabulous.core.api.manager.impl.cooldown;

import me.alpho320.fabulous.core.api.manager.IManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CooldownManager implements IManager {

    private final Map<UUID, List<Cooldown>> cooldowns = new HashMap<>();

    public boolean remove(UUID id, UUID cooldownID) {
        if (!cooldowns.containsKey(id)) return false;

        boolean removed = false;
        for (Cooldown cooldown : cooldowns.get(id)) {
            if (!cooldown.getId().equals(cooldownID))
                continue;

            cooldowns.get(id).remove(cooldown);
            removed = true;
        }

        return removed;
    }

    public boolean remove(UUID id) {
        if (!cooldowns.containsKey(id)) return false;

        cooldowns.remove(id);
        return true;
    }

    public boolean add(UUID id, Cooldown cooldown) {
        if (cooldown == null) return false;
        List<Cooldown> list = getCooldowns(id);

        list.add(cooldown);
        cooldowns.put(id, list);

        return true;
    }

    public List<Cooldown> getCooldowns(UUID id) {
        return cooldowns.getOrDefault(id, new ArrayList<>());
    }

    @Override
    public boolean setup() {
        return true;
    }
}
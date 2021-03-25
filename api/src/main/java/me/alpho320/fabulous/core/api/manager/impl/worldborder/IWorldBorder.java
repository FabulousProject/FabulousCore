package me.alpho320.fabulous.core.api.manager.impl.worldborder;

import java.util.List;
import java.util.UUID;

public interface IWorldBorder {

    UUID id();

    void send(Object player);
    void send(List<Object> players);
    
    void remove(Object player);
    void remove(List<Object> players);

    void update();

    Object center();
    void setCenter(Object location);

    WorldBorderManager.BorderColor color();
    void setColor(WorldBorderManager.BorderColor hBorderColor);

    double size();
    void setSize(double size);

    double damageAmount();
    void setDamageAmount(double damageAmount);

    double damageBuffer();
    void setDamageBuffer(double damageBuffer);

    int warningDistance();
    void setWarningDistance(int warningDistance);

    int warningTime();
    void setWarningTime(int warningTime);

    List<Object> players();
    void setPlayers(List<Object> players);

}
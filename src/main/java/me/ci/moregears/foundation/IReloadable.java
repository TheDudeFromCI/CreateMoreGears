package me.ci.moregears.foundation;

public interface IReloadable {

    boolean hasAmmo();

    int getReloadTicksRemaining();

    int getCooldownTicksRemaining();

    int getUnloadTicksRemaining();

    int getReloadTicks();

    int getCooldownTicks();

    int getUnloadTicks();
}

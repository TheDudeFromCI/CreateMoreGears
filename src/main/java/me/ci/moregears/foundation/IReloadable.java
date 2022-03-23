package me.ci.moregears.foundation;

public interface IReloadable {

    boolean hasAmmo();

    boolean isReloading();

    default int getReloadTicks() {
        return 60;
    }

    int getReloadTicksRemaining();

    void reload();
}

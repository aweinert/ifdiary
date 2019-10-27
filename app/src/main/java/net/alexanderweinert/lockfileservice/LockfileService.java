package net.alexanderweinert.lockfileservice;

import android.content.Context;

import java.io.File;
import java.io.IOException;

public abstract class LockfileService {
    public static LockfileService getInstance(Context applicationContext) {
        return new LockfileServiceImpl(applicationContext);
    }

    public abstract File createLockfile() throws IOException;

    public abstract void removeLockfile(File lockfile);
}

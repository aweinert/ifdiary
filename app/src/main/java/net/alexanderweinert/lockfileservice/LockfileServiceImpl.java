package net.alexanderweinert.lockfileservice;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

class LockfileServiceImpl extends LockfileService {
    private final Context applicationContext;

    private final static String lockfileFolder = "lockfiles";

    public LockfileServiceImpl(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public File createLockfile() throws IOException {
        final String uuid = UUID.randomUUID().toString();
        final String lockfileName = String.format("%s.lock", uuid);
        final File lockfile = new File(new File(applicationContext.getFilesDir(), lockfileFolder), lockfileName);
        lockfile.createNewFile();
        return lockfile;
    }

    @Override
    public void removeLockfile(File lockfile) {
        lockfile.delete();
    }
}

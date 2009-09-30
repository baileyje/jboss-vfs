/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.vfs.spi;

import org.jboss.vfs.VirtualFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

/**
 * A real filesystem.
 */
public final class RealFileSystem extends AbstractRefCounted implements FileSystem {

    /**
     * The root real filesystem (singleton instance).
     */
    public static final RealFileSystem ROOT_INSTANCE = new RealFileSystem(new File(""));

    private final File realRoot;

    /**
     * Construct a real filesystem with the given real root.
     *
     * @param realRoot the real root
     */
    public RealFileSystem(File realRoot) {
        this.realRoot = realRoot;
    }

    /**
     * {@inheritDoc}
     */
    public InputStream openInputStream(VirtualFile mountPoint, VirtualFile target) throws IOException {
        return new FileInputStream(getFile(mountPoint, target));
    }

    /**
     * {@inheritDoc}
     */
    public boolean isReadOnly() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public File getFile(VirtualFile mountPoint, VirtualFile target) {
        if (mountPoint.equals(target)) {
            return realRoot;
        } else {
            return new File(getFile(mountPoint, target.getParent()), target.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean delete(VirtualFile mountPoint, VirtualFile target) {
        return getFile(mountPoint, target).delete();
    }

    /**
     * {@inheritDoc}
     */
    public long getSize(VirtualFile mountPoint, VirtualFile target) throws IOException {
        return getFile(mountPoint, target).length();
    }

    /**
     * {@inheritDoc}
     */
    public long getLastModified(VirtualFile mountPoint, VirtualFile target) throws IOException {
        return getFile(mountPoint, target).lastModified();
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists(VirtualFile mountPoint, VirtualFile target) {
        return getFile(mountPoint, target).exists();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDirectory(VirtualFile mountPoint, VirtualFile target) {
        return getFile(mountPoint, target).isDirectory();
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getDirectoryEntries(VirtualFile mountPoint, VirtualFile target) {
        final String[] names = getFile(mountPoint, target).list();
        return names == null ? Collections.<String>emptyList() : Arrays.asList(names);
    }

    /** {@inheritDoc} */
    public Handle<? extends RealFileSystem> getHandle() throws IOException {
        return doGetHandle();
    }

    /**
     * {@inheritDoc}
     */
    protected void doClose() throws IOException {
        // no operation - the real FS can't be closed
    }
}

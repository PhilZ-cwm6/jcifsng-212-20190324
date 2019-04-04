/*
 * © 2016 AgNO3 Gmbh & Co. KG
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jcifsng212.context;


import java.net.MalformedURLException;
import java.net.URLStreamHandler;

import jcifsng212.BufferCache;
import jcifsng212.CIFSException;
import jcifsng212.Configuration;
import jcifsng212.Credentials;
import jcifsng212.DfsResolver;
import jcifsng212.NameServiceClient;
import jcifsng212.SidResolver;
import jcifsng212.SmbPipeResource;
import jcifsng212.SmbResource;
import jcifsng212.SmbTransportPool;
import jcifsng212.netbios.NameServiceClientImpl;
import jcifsng212.smb.BufferCacheImpl;
import jcifsng212.smb.CredentialsInternal;
import jcifsng212.smb.DfsImpl;
import jcifsng212.smb.Handler;
import jcifsng212.smb.NtlmPasswordAuthenticator;
import jcifsng212.smb.SIDCacheImpl;
import jcifsng212.smb.SmbFile;
import jcifsng212.smb.SmbNamedPipe;
import jcifsng212.smb.SmbTransportPoolImpl;


/**
 * @author mbechler
 *
 */
public class BaseContext extends AbstractCIFSContext {

    private final Configuration config;
    private final DfsResolver dfs;
    private final SidResolver sidResolver;
    private final Handler urlHandler;
    private final NameServiceClient nameServiceClient;
    private final BufferCache bufferCache;
    private final SmbTransportPool transportPool;
    private final CredentialsInternal defaultCredentials;


    /**
     * Construct a context
     * 
     * @param config
     *            configuration for the context
     * 
     */
    public BaseContext ( Configuration config ) {
        this.config = config;
        this.dfs = new DfsImpl(this);
        this.sidResolver = new SIDCacheImpl(this);
        this.urlHandler = new Handler(this);
        this.nameServiceClient = new NameServiceClientImpl(this);
        this.bufferCache = new BufferCacheImpl(this.config);
        this.transportPool = new SmbTransportPoolImpl();
        this.defaultCredentials = new NtlmPasswordAuthenticator();
    }


    /**
     * {@inheritDoc}
     * 
     * @throws CIFSException
     * 
     * @see jcifsng212.CIFSContext#get(java.lang.String)
     */
    @Override
    public SmbResource get ( String url ) throws CIFSException {
        try {
            return new SmbFile(url, this);
        }
        catch ( MalformedURLException e ) {
            throw new CIFSException("Invalid URL " + url, e);
        }
    }


    /**
     * 
     * {@inheritDoc}
     *
     * @see jcifsng212.CIFSContext#getPipe(java.lang.String, int)
     */
    @Override
    public SmbPipeResource getPipe ( String url, int pipeType ) throws CIFSException {
        try {
            return new SmbNamedPipe(url, pipeType, this);
        }
        catch ( MalformedURLException e ) {
            throw new CIFSException("Invalid URL " + url, e);
        }
    }


    @Override
    public SmbTransportPool getTransportPool () {
        return this.transportPool;
    }


    /**
     * {@inheritDoc}
     *
     * @see jcifsng212.CIFSContext#getConfig()
     */
    @Override
    public Configuration getConfig () {
        return this.config;
    }


    /**
     * {@inheritDoc}
     *
     * @see jcifsng212.CIFSContext#getDfs()
     */
    @Override
    public DfsResolver getDfs () {
        return this.dfs;
    }


    /**
     * {@inheritDoc}
     *
     * @see jcifsng212.CIFSContext#getNameServiceClient()
     */
    @Override
    public NameServiceClient getNameServiceClient () {
        return this.nameServiceClient;
    }


    /**
     * {@inheritDoc}
     *
     * @see jcifsng212.CIFSContext#getBufferCache()
     */
    @Override
    public BufferCache getBufferCache () {
        return this.bufferCache;
    }


    /**
     * {@inheritDoc}
     *
     * @see jcifsng212.CIFSContext#getUrlHandler()
     */
    @Override
    public URLStreamHandler getUrlHandler () {
        return this.urlHandler;
    }


    /**
     * {@inheritDoc}
     *
     * @see jcifsng212.CIFSContext#getSIDResolver()
     */
    @Override
    public SidResolver getSIDResolver () {
        return this.sidResolver;
    }


    /**
     * {@inheritDoc}
     *
     * @see jcifsng212.context.AbstractCIFSContext#getDefaultCredentials()
     */
    @Override
    protected Credentials getDefaultCredentials () {
        return this.defaultCredentials;
    }


    /**
     * {@inheritDoc}
     *
     * @see jcifsng212.CIFSContext#close()
     */
    @Override
    public boolean close () throws CIFSException {
        boolean inUse = super.close();
        inUse |= this.transportPool.close();
        return inUse;
    }

}

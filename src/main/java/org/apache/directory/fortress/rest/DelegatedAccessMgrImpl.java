/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.rest;

import org.apache.directory.fortress.core.DelAccessMgr;
import org.apache.directory.fortress.core.DelAccessMgrFactory;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.rbac.RolePerm;
import org.apache.directory.fortress.core.rbac.UserAdminRole;
import org.apache.directory.fortress.core.rbac.Permission;
import org.apache.directory.fortress.core.rbac.Role;
import org.apache.directory.fortress.core.rbac.Session;
import org.apache.directory.fortress.core.rbac.User;
import org.apache.directory.fortress.core.rbac.UserRole;
import org.apache.directory.fortress.core.rest.FortRequest;
import org.apache.directory.fortress.core.rest.FortResponse;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * Utility for EnMasse Server.  This class is thread safe.
 *
 * @author Shawn McKinney
 */
class DelegatedAccessMgrImpl
{
    private static final String CLS_NM = DelegatedAccessMgrImpl.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);

    /**
     * ************************************************************************************************************************************
     * BEGIN DELEGATEDACCESSMGR
     * **************************************************************************************************************************************
     */

    FortResponse canAssign(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            UserRole uRole = (UserRole) request.getEntity();
            Session session = request.getSession();
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            boolean result = accessMgr.canAssign(session, new User(uRole.getUserId()), new Role(uRole.getName()));
            response.setSession(session);
            response.setAuthorized(result);
            response.setErrorCode(0);
        }
        catch (org.apache.directory.fortress.core.SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }

    FortResponse canDeassign(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            UserRole uRole = (UserRole) request.getEntity();
            Session session = request.getSession();
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            boolean result = accessMgr.canDeassign(session, new User(uRole.getUserId()), new Role(uRole.getName()));
            response.setSession(session);
            response.setAuthorized(result);
            response.setErrorCode(0);
        }
        catch (SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }

    FortResponse canGrant(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            RolePerm context = (RolePerm) request.getEntity();
            Session session = request.getSession();
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            boolean result = accessMgr.canGrant(session, new Role(context.getRole().getName()), context.getPerm());
            response.setSession(session);
            response.setAuthorized(result);
            response.setErrorCode(0);
        }
        catch (SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }

    FortResponse canRevoke(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            RolePerm context = (RolePerm) request.getEntity();
            Session session = request.getSession();
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            boolean result = accessMgr.canRevoke(session, new Role(context.getRole().getName()), context.getPerm());
            response.setSession(session);
            response.setAuthorized(result);
            response.setErrorCode(0);
        }
        catch (SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }

    public FortResponse checkAdminAccess(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            Permission perm = (Permission) request.getEntity();
            Session session = request.getSession();
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            perm.setAdmin(true);
            boolean result = accessMgr.checkAccess(session, perm);
            response.setSession(session);
            response.setAuthorized(result);
            response.setErrorCode(0);
        }
        catch (SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }

    FortResponse addActiveAdminRole(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            UserAdminRole uAdminRole = (UserAdminRole) request.getEntity();
            Session session = request.getSession();
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            accessMgr.addActiveRole(session, uAdminRole);
            response.setSession(session);
            response.setErrorCode(0);
        }
        catch (SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }

    FortResponse dropActiveAdminRole(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            UserAdminRole uAdminRole = (UserAdminRole) request.getEntity();
            Session session = request.getSession();
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            accessMgr.dropActiveRole(session, uAdminRole);
            response.setSession(session);
            response.setErrorCode(0);
        }
        catch (SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }

    FortResponse sessionAdminRoles(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            Session session = request.getSession();
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            List<UserAdminRole> roles = accessMgr.sessionAdminRoles(session);
            response.setEntities(roles);
            response.setErrorCode(0);
        }
        catch (SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }

    FortResponse sessionAdminPermissions(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            Session session = request.getSession();
            List<Permission> perms = accessMgr.sessionPermissions(session);
            response.setSession(session);
            response.setEntities(perms);
            response.setErrorCode(0);
        }
        catch (SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }

    FortResponse authorizedSessionRoles(FortRequest request)
    {
        FortResponse response = new FortResponse();
        try
        {
            DelAccessMgr accessMgr = DelAccessMgrFactory.createInstance(request.getContextId());
            Session session = request.getSession();
            Set<String> roles = accessMgr.authorizedAdminRoles(session);
            response.setValueSet(roles);
            response.setSession(session);
            response.setErrorCode(0);
        }
        catch (SecurityException se)
        {
            log.info(CLS_NM + " caught " + se);
            response.setErrorCode(se.getErrorId());
            response.setErrorMessage(se.getMessage());
        }
        return response;
    }
}
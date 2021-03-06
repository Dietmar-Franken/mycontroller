/**
 * Copyright (C) 2015-2016 Jeeva Kandasamy (jkandasa@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mycontroller.standalone.db.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mycontroller.standalone.ObjectFactory;
import org.mycontroller.standalone.api.jaxrs.mapper.Query;
import org.mycontroller.standalone.api.jaxrs.mapper.QueryResponse;
import org.mycontroller.standalone.db.DaoUtils;
import org.mycontroller.standalone.db.tables.Role;
import org.mycontroller.standalone.db.tables.RoleGatewayMap;
import org.mycontroller.standalone.db.tables.RoleNodeMap;
import org.mycontroller.standalone.db.tables.RoleSensorMap;
import org.mycontroller.standalone.db.tables.RoleUserMap;
import org.mycontroller.standalone.db.tables.SensorVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.2
 */
public class RoleDaoImpl extends BaseAbstractDaoImpl<Role, Integer> implements RoleDao {
    private static final Logger _logger = LoggerFactory.getLogger(RoleDaoImpl.class);

    public RoleDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Role.class);
    }

    @Override
    public List<Role> getAll(List<Integer> ids) {
        return super.getAll(Role.KEY_ID, ids);
    }

    @Override
    public Role get(Role role) {
        return super.getById(role.getId());
    }

    @Override
    public Role getByRoleName(String roleName) {
        List<Role> roles = super.getAll(Role.KEY_NAME, roleName);
        if (roles != null && !roles.isEmpty()) {
            return roles.get(0);
        }
        return null;
    }

    @Override
    public QueryResponse getAll(Query query) {
        try {
            return super.getQueryResponse(query, Role.KEY_ID);
        } catch (SQLException ex) {
            _logger.error("unable to run query:[{}]", query, ex);
            return null;
        }
    }

    @Override
    public List<String> getPermissionsByUserId(Integer userId) {
        List<String> permissions = new ArrayList<String>();
        try {
            QueryBuilder<RoleUserMap, Object> roleUserQuery = DaoUtils.getRoleUserMapDao().getDao().queryBuilder();
            roleUserQuery.selectColumns(RoleUserMap.KEY_ROLE_ID).where().eq(RoleUserMap.KEY_USER_ID, userId);
            List<Role> roles = this.getDao().queryBuilder().selectColumns(Role.KEY_PERMISSION).distinct()
                    .join(roleUserQuery)
                    .query();
            for (Role role : roles) {
                permissions.add(role.getPermission().getText());
            }
        } catch (SQLException ex) {
            _logger.error("Exception, ", ex);
        }
        return permissions;
    }

    @Override
    public List<Integer> getGatewayIds(Integer userId) {
        List<Integer> ids = new ArrayList<Integer>();
        try {
            //Get role ids for this user
            List<Integer> roleIds = DaoUtils.getRoleUserMapDao().getRolesByUserId(userId);
            _logger.debug("RoleIds:{}", roleIds);
            //if role id is not null do not execute
            if (roleIds != null && roleIds.size() != 0) {
                QueryBuilder<RoleGatewayMap, Object> roleGatewayQuery = DaoUtils.getRoleGatewayMapDao().getDao()
                        .queryBuilder();
                List<RoleGatewayMap> roleGatewayMaps = roleGatewayQuery.where()
                        .in(RoleGatewayMap.KEY_ROLE_ID, roleIds).query();
                for (RoleGatewayMap roleGatewayMap : roleGatewayMaps) {
                    ids.add(roleGatewayMap.getGateway().getId());
                }
            }

        } catch (SQLException ex) {
            _logger.error("Exception, ", ex);
        }
        return ids;
    }

    @Override
    public List<Integer> getNodeIds(Integer userId) {
        List<Integer> ids = new ArrayList<Integer>();
        try {
            //Get role ids for this user
            List<Integer> roleIds = DaoUtils.getRoleUserMapDao().getRolesByUserId(userId);
            //if role id is not null do not execute
            if (roleIds != null && roleIds.size() != 0) {
                QueryBuilder<RoleNodeMap, Object> roleNodeQuery = DaoUtils.getRoleNodeMapDao().getDao()
                        .queryBuilder();
                List<RoleNodeMap> roleNodeMaps = roleNodeQuery.where()
                        .in(RoleGatewayMap.KEY_ROLE_ID, roleIds).query();
                for (RoleNodeMap roleNodeMap : roleNodeMaps) {
                    ids.add(roleNodeMap.getNode().getId());
                }
            }
            if (ObjectFactory.getAppProperties().getControllerSettings().getGrantAccessToChildResources()) {
                ids.addAll(DaoUtils.getNodeDao().getNodeIdsByGatewayIds(getGatewayIds(userId)));
            }
        } catch (SQLException ex) {
            _logger.error("Exception, ", ex);
        }
        return ids;
    }

    @Override
    public List<Integer> getSensorIds(Integer userId) {
        List<Integer> ids = new ArrayList<Integer>();
        try {
            //Get role ids for this user
            List<Integer> roleIds = DaoUtils.getRoleUserMapDao().getRolesByUserId(userId);
            //if role id is not null do not execute
            if (roleIds != null && roleIds.size() != 0) {
                QueryBuilder<RoleSensorMap, Object> roleSensorQuery = DaoUtils.getRoleSensorMapDao().getDao()
                        .queryBuilder();
                List<RoleSensorMap> roleSensorMaps = roleSensorQuery.where()
                        .in(RoleSensorMap.KEY_ROLE_ID, roleIds).query();
                for (RoleSensorMap roleSensorMap : roleSensorMaps) {
                    ids.add(roleSensorMap.getSensor().getId());
                }
            }
            if (ObjectFactory.getAppProperties().getControllerSettings().getGrantAccessToChildResources()) {
                ids.addAll(DaoUtils.getSensorDao().getSensorIdsByNodeIds(getNodeIds(userId)));
            }
        } catch (SQLException ex) {
            _logger.error("Exception, ", ex);
        }
        return ids;
    }

    @Override
    public List<Integer> getSensorVariableIds(Integer userId) {
        List<Integer> ids = new ArrayList<Integer>();
        //if role id is not null do not execute
        List<SensorVariable> sensorVariables = DaoUtils.getSensorVariableDao().getAllBySensorIds(getSensorIds(userId));
        for (SensorVariable sensorVariable : sensorVariables) {
            ids.add(sensorVariable.getId());
        }
        return ids;
    }

}

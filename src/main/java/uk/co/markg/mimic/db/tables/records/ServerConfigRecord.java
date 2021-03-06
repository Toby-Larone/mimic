/*
 * This file is generated by jOOQ.
 */
package uk.co.markg.mimic.db.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import uk.co.markg.mimic.db.tables.ServerConfig;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ServerConfigRecord extends UpdatableRecordImpl<ServerConfigRecord> implements Record2<Long, String> {

    private static final long serialVersionUID = 742104104;

    /**
     * Setter for <code>server_config.serverid</code>.
     */
    public void setServerid(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>server_config.serverid</code>.
     */
    public Long getServerid() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>server_config.opt_in_role</code>.
     */
    public void setOptInRole(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>server_config.opt_in_role</code>.
     */
    public String getOptInRole() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return ServerConfig.SERVER_CONFIG.SERVERID;
    }

    @Override
    public Field<String> field2() {
        return ServerConfig.SERVER_CONFIG.OPT_IN_ROLE;
    }

    @Override
    public Long component1() {
        return getServerid();
    }

    @Override
    public String component2() {
        return getOptInRole();
    }

    @Override
    public Long value1() {
        return getServerid();
    }

    @Override
    public String value2() {
        return getOptInRole();
    }

    @Override
    public ServerConfigRecord value1(Long value) {
        setServerid(value);
        return this;
    }

    @Override
    public ServerConfigRecord value2(String value) {
        setOptInRole(value);
        return this;
    }

    @Override
    public ServerConfigRecord values(Long value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ServerConfigRecord
     */
    public ServerConfigRecord() {
        super(ServerConfig.SERVER_CONFIG);
    }

    /**
     * Create a detached, initialised ServerConfigRecord
     */
    public ServerConfigRecord(Long serverid, String optInRole) {
        super(ServerConfig.SERVER_CONFIG);

        set(0, serverid);
        set(1, optInRole);
    }
}

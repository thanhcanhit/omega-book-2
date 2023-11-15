/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @author KienTran
 */
public final class Shift {

    private final String ID_EMPTY = "ID không được rỗng !";
    private final String STARTEDAT_ERROR = "StartedAt không được rỗng !";
    private final String ENDEDAD_ERROR = "EndedAt Không được rỗng !";
    private final String ACCOUNT_ERROR = "Account không được rỗng !";

    private String shiftID;
    private Date startedAt;
    private Date endedAt;
    private Account account;

    public Shift(String shiftID, Date startedAt, Account account) {
        this.shiftID = shiftID;
        this.startedAt = startedAt;
        this.account = account;
    }

    public String getShiftID() {
        return shiftID;
    }

    public void setShiftID(String shiftID) throws Exception {
        String regex = "^[P][H]\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(shiftID).matches()) {
            this.shiftID = shiftID;
        } else {
            throw new Exception(ID_EMPTY);
        }

    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) throws Exception {
        if (startedAt != null) {
            this.startedAt = startedAt;
        } else {
            throw new Exception(STARTEDAT_ERROR);
        }
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) throws Exception {
        if (endedAt != null) {
            this.endedAt = endedAt;
        } else {
            throw new Exception(ENDEDAD_ERROR);
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) throws Exception {
        if (account != null) {
            this.account = account;
        } else {
            throw new Exception(ACCOUNT_ERROR);
        }
    }

    public Shift(String shiftID, Date startedAt, Date endedAt, Account account) throws Exception {
        setAccount(account);
        setStartedAt(startedAt);
        setEndedAt(endedAt);
        setShiftID(shiftID);
    }

    public Shift(String shiftID) {
        this.shiftID = shiftID;
    }

    public Shift() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.shiftID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Shift other = (Shift) obj;
        return Objects.equals(this.shiftID, other.shiftID);
    }

    @Override
    public String toString() {
        return "Shift{" + "STARTEDAT_ERROR=" + STARTEDAT_ERROR + ", ENDEDAD_ERROR=" + ENDEDAD_ERROR + ", ACCOUNT_ERROR=" + ACCOUNT_ERROR + ", shiftID=" + shiftID + ", startedAt=" + startedAt + ", endedAt=" + endedAt + ", account=" + account + '}';
    }

}

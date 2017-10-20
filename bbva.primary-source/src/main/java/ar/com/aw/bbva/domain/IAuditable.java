package ar.com.aw.bbva.domain;

import java.util.Date;

public abstract class IAuditable {

    public abstract String getAudUsrIns();

    public abstract void setAudUsrIns(String audUsrIns);

    public abstract Date getAudFechaIns();

    public abstract void setAudFechaIns(Date audFechaIns);

    public abstract String getAudUsrUpd();

    public abstract void setAudUsrUpd(String audUsrUpd);

    public abstract Date getAudFechaUpd();

    public abstract void setAudFechaUpd(Date audFechaUpd);

    public void auditar() {
//		String usuario = ThreadContext.get().getUserName();
        String usuario = "DESA";
        if (getAudFechaIns() == null && getAudUsrIns() == null) {
            setAudFechaIns(new Date());
            setAudUsrIns(usuario);
        }
        setAudFechaUpd(new Date());
        setAudUsrUpd(usuario);
    }

}

//package com.labpro;
//
package com.labpro;

import java.util.List;

//// TODO: KurirPageController must have object of this class as its attribute
public class ProxyPengiriman implements IProxyPengiriman {
    private RepoPengirimanController repo;

    public ProxyPengiriman(RepoPengirimanController repo) {
        this.repo = repo;
    }

    public void updateStatus(int idPengiriman, StatusPengiriman status) {
        repo.updateStatus(idPengiriman, status);
    }

    public List<Pengiriman> getPengirimanByKurir(Kurir kurir){
        return repo.getPengirimanByKurir(kurir);
    }
}

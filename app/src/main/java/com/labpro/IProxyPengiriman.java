package com.labpro;

import java.util.List;

// TODO: RepoPengirimanController implement this interface
public interface IProxyPengiriman {
    public void updateStatus(int idPengiriman, StatusPengiriman status);
    public List<Pengiriman> getPengirimanByKurir(Kurir kurir);
}

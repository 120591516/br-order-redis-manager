package br.order.redis.org;

import br.crm.pojo.org.OrganizationSale;

public interface OrgSaleRedis {
    void initData();

    int setOrganizationSale(OrganizationSale organizationSale);

    OrganizationSale getOrganizationSale(Long orgSaleId);

    int deleteOrganizationSale(Long orgSaleId);
}

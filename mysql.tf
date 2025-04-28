resource "azurerm_virtual_network" "vn" {
  name                = "dtsvn"
  location            = "UK South"
  resource_group_name = var.resource_group_name
  address_space       = ["10.0.0.0/16"]
}

resource "azurerm_subnet" "dts-subnet" {
  name                 = "dts-sn"
  resource_group_name  = var.resource_group_name
  virtual_network_name = azurerm_virtual_network.vn.name
  address_prefixes     = ["10.0.2.0/24"]
  service_endpoints    = ["Microsoft.Storage"]
  delegation {
    name = "fs"
    service_delegation {
      name = "Microsoft.DBforMySQL/flexibleServers"
      actions = [
        "Microsoft.Network/virtualNetworks/subnets/join/action",
      ]
    }
  }
}

resource "azurerm_private_dns_zone" "dts-dns" {
  name                = "dts.mysql.database.azure.com"
  resource_group_name = var.resource_group_name
}

resource "azurerm_private_dns_zone_virtual_network_link" "dts-nl" {
  name                  = "dtsVnetZone.com"
  private_dns_zone_name = azurerm_private_dns_zone.dts-dns.name
  virtual_network_id    = azurerm_virtual_network.vn.id
  resource_group_name   = var.resource_group_name
}

resource "azurerm_mysql_flexible_server" "db" {
  name                   = "dts-mysql-db"
  resource_group_name    = var.resource_group_name
  location               = "UK South"
  administrator_login    = "adminuser"
  administrator_password = var.pass
  delegated_subnet_id    = azurerm_subnet.dts-subnet.id
  private_dns_zone_id    = azurerm_private_dns_zone.dts-dns.id
  sku_name               = "B_Standard_B1ms"

  depends_on = [azurerm_private_dns_zone_virtual_network_link.dts-nl]
}

resource "azurerm_mysql_flexible_database" "mydb" {
  name                = "dtsdatabase"
  resource_group_name = var.resource_group_name
  server_name         = azurerm_mysql_flexible_server.db.name
  charset            = "utf8mb4"
  collation          = "utf8mb4_unicode_ci"
}
output "kube_config" {
  value = azurerm_kubernetes_cluster.aks.kube_config_raw
  sensitive = true
}

output "acr_login_server" {
  value = azurerm_container_registry.acr.login_server
}

output "mysql_fqdn" {
  value = azurerm_mysql_flexible_server.db.fqdn
}

output "aks_cluster_name" {
  value = azurerm_kubernetes_cluster.aks.name
}

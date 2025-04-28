variable "location" {
  default = "UK South"
}

variable "resource_group_name" {
  default = "dts-developer-challenge"
}

variable "acr_name" {
  default = "dtsazurecr"
}

variable "aks_name" {
  default = "dtsakscluster"
}

variable "pass" {
    type = string
    default = "1234567890"
}

output "admin_credentials" {
  value = join(" ", ["admin", var.user_password])
}

output "user_credentials" {
  value = join(" ", ["user", var.user_password])
}

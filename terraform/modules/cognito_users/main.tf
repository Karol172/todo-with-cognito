resource "null_resource" "cognito_user" {

  triggers = {
    user_pool_id = var.user_pool_id
  }

  provisioner "local-exec" {
    command = "aws cognito-idp admin-create-user --user-pool-id ${var.user_pool_id} --username admin"
  }

  provisioner "local-exec" {
    command = "aws cognito-idp admin-add-user-to-group --user-pool-id ${var.user_pool_id} --username admin --group-name ${var.group_admin}"
  }

  provisioner "local-exec" {
    command = "aws cognito-idp admin-set-user-password --user-pool-id ${var.user_pool_id} --username admin --password ${var.user_password}"
  }

  provisioner "local-exec" {
    command = "aws cognito-idp admin-create-user --user-pool-id ${var.user_pool_id} --username user"
  }

  provisioner "local-exec" {
    command = "aws cognito-idp admin-add-user-to-group --user-pool-id ${var.user_pool_id} --username user --group-name ${var.group_users}"
  }

  provisioner "local-exec" {
    command = "aws cognito-idp admin-set-user-password --user-pool-id ${var.user_pool_id} --username user --password ${var.user_password}"
  }

}
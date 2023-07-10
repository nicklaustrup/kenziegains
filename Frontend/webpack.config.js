const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js'),
    registerUserPage: path.resolve(__dirname, 'src', 'pages', 'registerUserPage.js'),
    user_loginPage: path.resolve(__dirname, 'src', 'pages', 'user_loginPage.js'),
    user_registerPage: path.resolve(__dirname, 'src', 'pages', 'user_registerPage.js'),
    classes_administratorPage: path.resolve(__dirname, 'src', 'pages', 'classes_administratorPage.js'),
    class_createPage: path.resolve(__dirname, 'src', 'pages', 'class_createPage.js'),
    class_update_administratorPage: path.resolve(__dirname, 'src', 'pages', 'class_update_administratorPage.js'),
    classes_instructorPage: path.resolve(__dirname, 'src', 'pages', 'classes_instructorPage.js'),
    class_create_instructorPage: path.resolve(__dirname, 'src', 'pages', 'class_create_instructorPage.js'),
    class_update_instructorPage: path.resolve(__dirname, 'src', 'pages', 'class_update_instructorPage.js'),
    classes_gymmemberPage: path.resolve(__dirname, 'src', 'pages', 'classes_gymmemberPage.js'),
    class_create_registrationPage: path.resolve(__dirname, 'src', 'pages', 'class_create_registrationPage.js'),
    class_update_registrationPage: path.resolve(__dirname, 'src', 'pages', 'class_update_registrationPage.js'),
    user_updatePage: path.resolve(__dirname, 'src', 'pages', 'user_updatePage.js'),

  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 8080,
    open: true,
    proxy: [
      {
        context: [
          '/example',
        ],
        target: 'http://localhost:5001'
      },
      {
        context: [
          '/user',
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/registerUser.html',
      filename: 'registerUser.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/user_login.html',
      filename: 'user_login.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/user_register.html',
      filename: 'user_register.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/classes_administrator.html',
      filename: 'classes_administrator.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/class_create.html',
      filename: 'class_create.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/class_update_administrator.html',
      filename: 'class_update_administrator.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/classes_instructor.html',
      filename: 'classes_instructor.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/class_update_instructor.html',
      filename: 'class_update_instructor.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/class_create_instructor.html',
      filename: 'class_create_instructor.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/classes_gymmember.html',
      filename: 'classes_gymmember.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/class_create_registration.html',
      filename: 'class_create_registration.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/class_update_registration.html',
      filename: 'class_update_registration.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/user_update.html',
      filename: 'user_update.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}

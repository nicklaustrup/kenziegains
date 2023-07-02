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

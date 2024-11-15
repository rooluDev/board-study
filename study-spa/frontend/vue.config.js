const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,

  outputDir: './build',

  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8081/',
        changeOrigin: true,
      },
    },
  },
});

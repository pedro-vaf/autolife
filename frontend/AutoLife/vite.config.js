import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [
    react({
      // Configuração do React com JSX
      jsxRuntime: 'automatic'
    })
  ],

  // Otimizações de dependências
  optimizeDeps: {
    include: ['react', 'react-dom']
  },
})
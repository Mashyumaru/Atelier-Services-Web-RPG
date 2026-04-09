/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        medieval: ['Grenze Gotisch', 'serif'],
      },
      colors: {
        sepia: {
          100: '#f4ece3',
          200: '#e8d9c7',
          300: '#d9c2a7',
          400: '#c6a682',
          600: '#a67c52',
          700: '#8c6239',
          800: '#6f4e30',
          900: '#513923',
          950: '#312215',
        },
        parchment: {
          100: '#fcf5e5',
        }
      },
    },
  },
  plugins: [],
}
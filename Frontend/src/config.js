// Configuration based on environment
const config = {
    // Get the API URL from environment or use Elastic Beanstalk URL
    apiUrl: ''
  };
  
  // For local development, use localhost
  if (window.location.hostname === 'localhost') {
    config.apiUrl = 'http://localhost:5001';
  }
  
  
  export default config;


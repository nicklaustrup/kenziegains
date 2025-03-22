// Configuration based on environment
const config = {
    // Get the API URL from environment or use Elastic Beanstalk URL
    apiUrl: process.env.REACT_APP_API_URL || 'http://kenziegains-elasticbeanstalk.eba-hbpfp9z6.us-west-2.elasticbeanstalk.com'
  };
  
  // For local development, use localhost
  if (window.location.hostname === 'localhost') {
    config.apiUrl = 'http://localhost:5001';
  }
  
  export default config;
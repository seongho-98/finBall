import * as React from 'react';

const Logo: React.FC = ({ value }) => {

  return (
    <div style={{ textAlign: "center", alignItems: "center", display: "flex", width: '280px', height: '10vh' }}>
      <img src={value.company.logo} alt="" style={{ width: '55px',height:'55px',marginLeft:"0px" }}/>
      <div style={{ width: '200px',position: 'absolute',left:"80px",display:"flex",flexDirection:"column"}}>
        <div style={{textAlign:'start'}}>
        {value.account.name}
        </div>
        <div style={{textAlign:'start',fontSize:"2px",marginLeft:"2px",opacity:"0.5"}}>
        {value.company.name}
        {value.account.no}

        </div>
      </div>
    </div>
  );
}

export default Logo;
